import greenfoot.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A client that reads data from the Kinect server via a socket.
 */
public class KinectClient  
{
    private SocketChannel socket; //null if we aren't connected
    private String host;
    private HashMap<Integer, UserData> users = new HashMap<Integer, UserData>();
    private final GreenfootImage thumbnail;
    private final GreenfootImage combinedUserImage;
    private short maxDepth = 0;
    private final short[] depthArray; // null if we're not using it
    private final int thumbnailWidth;
    private final int thumbnailHeight;
    private final ByteBuffer thumbnailDirectBuffer;
    
    /** The width of the image from Kinect when not scaled */
    public static final int DEFAULT_IMAGE_WIDTH = 640;
    /** The height of the image from Kinect when not scaled */
    public static final int DEFAULT_IMAGE_HEIGHT = 480;
    
    /**
     * Creates a KinectClient that attempts to connect to a server on the local machine,
     * and that uses a full-size image from the Kinect feed (640 by 480) and does not
     * receive depth information.
     * 
     * You can use the isConnected() method after construction to check that the client
     * was able to connect to the server.
     */
    public KinectClient()
    {
        this(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT, false);
    }
    
    /**
     * Creates a KinectClient that attempts to connect to a server on the local machine.
     * 
     * The first two parameters determine the width and height of the thumbnail.  This should
     * be 640, 480 for the full-size image, or some exact division if you only need a smaller
     * image (this can help performance) like 320, 240 or 80, 60.
     *
     * The third parameter specifies whether to make available depth information
     * for each pixel.  It is recommended that you pass false (as this will save resources)
     * unless you know you want this.
     * 
     * You can use the isConnected() method after construction to check that the client
     * was able to connect to the server.
     */
    public KinectClient(int thumbnailWidth, int thumbnailHeight, boolean depth)
    {
        this(thumbnailWidth, thumbnailHeight, depth, "127.0.0.1");
    }

    
    /**
     * Creates a KinectClient that attempts to connect to the specified server
     * ("127.0.0.1" by default).
     * 
     * The first two parameters determine the width and height of the thumbnail.  This should
     * be 640, 480 for the full-size image, or some exact division if you only need a smaller
     * image (this can help performance) like 320, 240 or 80, 60.
     *
     * The third parameter specifies whether to make available depth information
     * for each pixel.  It is recommended that you pass false (as this will save resources)
     * unless you know you want this.
     * 
     * You can use the isConnected() method after construction to check that the client
     * was able to connect to the server.
     */
    public KinectClient(int thumbnailWidth, int thumbnailHeight, boolean depth, String host)
    {
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.host = host;
        if (thumbnailWidth != 0 && thumbnailHeight != 0)
        {
            this.thumbnail = new GreenfootImage(thumbnailWidth, thumbnailHeight);
            this.combinedUserImage = new GreenfootImage(thumbnailWidth, thumbnailHeight);
            this.thumbnailDirectBuffer = ByteBuffer.allocateDirect(thumbnailWidth * thumbnailHeight * 4);
        }
        else
        {
            this.thumbnail = null;
            this.combinedUserImage = null;
            this.thumbnailDirectBuffer = null;
        }

        if (depth)
        {
            this.depthArray = new short[DEFAULT_IMAGE_HEIGHT * DEFAULT_IMAGE_WIDTH];
        }
        else
        {
            this.depthArray = null;
        }

        
        try
        {
            connect();
        }
        catch (IOException e)
        {
            //System.err.println("Error connecting to server:");
            //e.printStackTrace();
        }
    }
    
    private void connect() throws IOException
    {
        try
        {
            socket = SocketChannel.open();
            socket.socket().setReceiveBufferSize(1048576);
            socket.connect(new InetSocketAddress(host, 0x2020));
            socket.configureBlocking(false);
            sendRequestForMore();
        }
        catch (IOException e)
        {
            socket = null;
            throw e;
        }
    }
    
    /**
     * Disconnects from the server.
     * 
     * It is a good idea to call this if you know you won't be using this KinectClient again.
     */
    public void disconnect()
    {
        if (socket == null)
            return;
        
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
        }
        socket = null;
    }

    // Sends request to the server for more data.
    // Sending this also stops the server outpacing the client and flooding the socket.
    private void sendRequestForMore() throws IOException
    {
        ByteBuffer buf = ByteBuffer.allocate(12);
        buf.clear();
        // We send 2 when we want depth data, 1 when we don't:
        buf.putInt(depthArray == null ? (int)1 : (int)2);
        buf.putInt(thumbnailWidth);
        buf.putInt(thumbnailHeight);
        buf.flip();
        socket.write(buf);
    }

    // Reads a Point3D from the socket, and scales it according to the parameter
    private static Point3D readPoint3D(ByteBuffer input) throws IOException
    {
        float x = input.getFloat();
        float y = input.getFloat();
        float z = input.getFloat();
        return new Point3D(x, y, z);
    }
    
    // Gets the given number of bytes from the socket into a returned ByteBuffer
    private ByteBuffer fillBufferFromSocket(int total) throws IOException
    {
        return fillBufferFromSocket(ByteBuffer.allocate(total), total);
    }
    
    private ByteBuffer fillBufferFromSocket(ByteBuffer buf, int total) throws IOException
    {
        buf.clear();
        while (total > 0)
        {
            int n = socket.read(buf);
            if (n == -1) throw new IOException("EOF");
            total -= n;
        }
        buf.flip();
        return buf;
    }
    
    private void bufferToImage(IntBuffer buf, BufferedImage img, int x, int y, int amount)
    {
        try {
            buf.get(((DataBufferInt)img.getRaster().getDataBuffer()).getData(), y*thumbnailWidth + x, amount);
        } catch (Exception e) {
            int[] thumbnailArray = new int[amount];
            buf.get(thumbnailArray);
            //First do the first scan line:
            if (x != 0) {
                img.setRGB(x, y, thumbnailWidth - x, 1, thumbnailArray, 0, thumbnailWidth);
                amount -= thumbnailWidth - x;
                y += 1;
            }
            //Then all complete scan lines in the middle:
            if (amount >= thumbnailWidth)
            {
                img.setRGB(0, y, thumbnailWidth, amount / thumbnailWidth, thumbnailArray, thumbnailArray.length - amount, thumbnailWidth);
                y += amount / thumbnailWidth;
                amount = amount % thumbnailWidth;
            }
            //Then the last scan line:
            if (amount > 0)
            {
                img.setRGB(0, y, amount, 1, thumbnailArray, thumbnailArray.length - amount, thumbnailWidth);
            }
        }
    }
       
    private static class ImageAndBounds
    {
        public GreenfootImage img;
        public int minX, maxX, minY, maxY;
        
        public ImageAndBounds(int w, int h)
        {
            img = new GreenfootImage(w, h);
            minX = w; maxX = 0; // Set up to be wrong ready for narrowing
            minY = h; maxY = 0;
        }
    }
    
    /**
     * Attempts to read the latest update from the server.
     * 
     * Should only be called once per frame (typically in the world's act() method), as there is no point receiving two updates per frame.
     * If we are not currently connected, the method does nothing.
     */
    public void update()
    {
        try {
            if (socket == null)
                return;
            
            Selector sel = Selector.open();
            socket.register(sel, SelectionKey.OP_READ, null);
            // Read only if there is any data available:
            if (sel.selectNow() > 0)
            {
                // All complete messages are preceded by their size:
                ByteBuffer buf = fillBufferFromSocket(4);
                long startTime = System.currentTimeMillis();
                int msgSize = buf.getInt();
                if (msgSize == 0)
                {
                    throw new IOException("Your kinectserver is too old, need at least version 1.1");
                }
                buf = fillBufferFromSocket(msgSize);
                
                // Then the number of users:
                int numUsers = buf.getInt();
                Set<Integer> presentUsers = new HashSet<Integer>();
                for (int i = 0; i < numUsers; i++)
                {
                    // Each user has an id, followed by state
                    int id = buf.getInt();
                    UserData u = users.get(id);
                    if (u == null)
                    {
                        u = new UserData(id);
                        users.put(id, u);
                    }
                    presentUsers.add(id);
                    
                    int state = buf.getInt();
                    u.setState(state);

                    // If they are being tracked, we are sent their skeleton data:
                    if (u.isTracking())
                    {
                        for (int j = 0; j < Joint.NUM_JOINTS; j++)
                        {
                            float conf = buf.getFloat();
                            Point3D posWorld = readPoint3D(buf);
                            Point3D posScreen = readPoint3D(buf);
                            u.setJoint(j, new Joint(j, conf, posWorld, posScreen));
                        }
                    }                    
                }
                // Remove any users who we aren't still getting data for:
                Set<Integer> oldUsers = users.keySet();
                oldUsers.retainAll(presentUsers);
                
                if (thumbnailWidth != 0 && thumbnailHeight != 0)
                {
                    
                    // Read the user labels:
                    buf = fillBufferFromSocket(4);
                    ShortBuffer userLabelBuffer = fillBufferFromSocket(buf.getInt()).asShortBuffer();
                                    
                    // Read the thumbnail:
                    IntBuffer thumbnailBuffer = fillBufferFromSocket(thumbnailDirectBuffer, thumbnailWidth * thumbnailHeight * 4).asIntBuffer();
                    bufferToImage(thumbnailBuffer, thumbnail.getAwtImage(), 0, 0, thumbnailWidth * thumbnailHeight);
                    
                    // Form the user images:
                    short curValue = 0;
                    short curCount = 0;
                    HashMap<Integer, ImageAndBounds> userImages = new HashMap<Integer, ImageAndBounds>();
                    for (UserData u : users.values())
                    {
                        userImages.put(u.getId(), new ImageAndBounds(thumbnailWidth, thumbnailHeight));
                    }
                    combinedUserImage.clear();
                    thumbnailBuffer.rewind();
                    int pos = 0;
                    while (pos < thumbnailHeight*thumbnailWidth)
                    {
                        curValue = userLabelBuffer.get();
                        if ((curValue & 0x8000) != 0)
                        {
                            curCount = (short)(curValue & 0x7FFF);
                            curValue = userLabelBuffer.get();
                        }
                        else
                        {
                            curCount = 1;
                        }
                            
                        //Place the pixel curValue:
                        if (curValue != 0)
                        {
                            final int x = pos % thumbnailWidth;
                            final int y = pos / thumbnailWidth;
                            thumbnailBuffer.position(pos);
                            bufferToImage(thumbnailBuffer, combinedUserImage.getAwtImage(), x, y, curCount);
                            ImageAndBounds iab = userImages.get((int)curValue);
                            thumbnailBuffer.position(pos);
                            if (iab != null)
                            {
                                bufferToImage(thumbnailBuffer, iab.img.getAwtImage(), x, y, curCount);
                                iab.minX = Math.min(iab.minX, x);
                                iab.maxX = Math.max(iab.maxX, x);
                                iab.minY = Math.min(iab.minY, y);
                                iab.maxY = Math.max(iab.maxY, y);
                            }
                        }
                        pos += curCount;
                    }
                    for (UserData u : users.values())
                    {
                        ImageAndBounds iab = userImages.get(u.getId());
                        if (iab.maxX >= iab.minX)
                        {
                            GreenfootImage img = new GreenfootImage(iab.maxX - iab.minX + 1, iab.maxY - iab.minY + 1);
                            img.drawImage(iab.img, -iab.minX, -iab.minY);
        
                            u.setImage(img, iab.minX, iab.minY);
                        }
                        else u.setImage(null, 0, 0);
                    }
                }

                if (depthArray != null)
                {
                    ShortBuffer depthBuffer = fillBufferFromSocket((1 + DEFAULT_IMAGE_WIDTH * DEFAULT_IMAGE_HEIGHT) * 2).asShortBuffer();
                    maxDepth = depthBuffer.get();
                    depthBuffer.get(depthArray);
                }
                
                // And ask for more data ready for next frame;
                // we should send this once per frame we receive, it acts like an ack:
                sendRequestForMore();
            }
            sel.close();
        }
        catch (IOException e)
        {
            socket = null;
            //e.printStackTrace();
        }
    }
    
    /**
     * Indicates whether the client is currently connected to the server.
     * 
     * If it returns true, that isn't a guarantee that it it will remain connected
     * (for example, the socket may have been dropped and we might not have noticed yet),
     * but if it returns false, that is a guarantee that it is not connected.
     */
    public boolean isConnected()
    {
        return socket != null;
    }
    
    /**
     * Gets the user data for all users detected by the sensor
     * (who may or may not have their skeletons currently tracked).
     * 
     * Never returns null, but may return an empty array.
     */
    public UserData[] getUsers()
    {
        return users.values().toArray(new UserData[0]);
    }
    
    /**
     * Gets the user data for a particular user id.
     * 
     * Returns null if there is no data for that user.
     */
    public UserData getUser(int userId)
    {
        return users.get(userId);
    }

    /**
     * Gets a picture of what the sensor
     * currently sees.  This thumbnail is of a fixed size determined by the parameters
     * passed to the KinectClient constructor (640 * 480 if not specified).
     * 
     * This method may return null if the client is not connected or if update
     * hasn't succesfully read a frame of data since it connected.
     */
    public GreenfootImage getThumbnail()
    {
        if (thumbnail == null)
            return null;
        else
        {
            return new GreenfootImage(thumbnail);
        }
    }
    
    /**
     * Gets an image of all the users the sensor currently sees.
     * 
     * This image is essentially the image from getThumbnail() (and will be the same size), but with all the pixels
     * that are not currently occupied by users turned to transparent.
     * 
     * This method may return null if the client is not connected or if update
     * hasn't succesfully read a frame of data since it connected.
     */
    public GreenfootImage getCombinedUserImage()
    {
        if (thumbnail == null)
            return null;
        else
            return new GreenfootImage(combinedUserImage);
    }

    /**
     * Gets the depth array (or null if not available).
     * The contents will change every update, so you should take a copy
     * if you want to use it across multiple frames, and you should
     * not alter its contents.  The numbers should be in the 
     * range 0 to getMaxDepth() inclusive.
     */
    public short[] getRawDepth()
    {
        return depthArray;
    }

    /**
     * Gets the maximum depth value (if depth is turned on, 0 if there is a problem).
     * You should be able to rely on this number being stable
     * from after the first update onwards.
     */
    public short getMaxDepth()
    {
        return maxDepth;
    }
}
