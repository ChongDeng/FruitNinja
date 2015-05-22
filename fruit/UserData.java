import greenfoot.*;
import java.awt.BasicStroke;

/**
 * The data for a user (i.e. person in front of the sensor).
 */
public class UserData
{
    private int id; // set by the Kinect software, arbitrary and gets re-used
    private int state; // current state, 0 is not tracking, 1 is calibrating, 2 is tracking
    private int previousState; // state before last data frame from the server.
    private final Joint[] joints = new Joint[Joint.NUM_JOINTS]; // Joint data, only valid when tracking
    private GreenfootImage img; // Scaled image, present in any state, can be arbitrary size
    private int imgX, imgY; // Scaled offset into overall scaled 640*480 image.

    private UserData(UserData ud, float scale)
    {
        this.id = ud.id;
        this.state = ud.state;
        this.previousState = ud.state;
        this.img = new GreenfootImage(ud.img);
        this.img.scale((int)(scale * this.img.getWidth()), (int)(scale * this.img.getHeight()));
        this.imgX = (int)(ud.imgX * scale);
        this.imgY = (int)(ud.imgY * scale);
        for (int i = 0; i < Joint.NUM_JOINTS; i++)
        {
            joints[i] = ud.joints[i].scaledCopy(scale);
        }
    }
    
    /**
     * For internal use only.
     */
    public UserData(int id)
    {
        this.id = id;
        previousState = -1;
    }
    
    /**
     * For internal use only.
     */
    public void setState(int state)
    {
        this.previousState = this.state;
        this.state = state;
    }
    
    /**
     * For internal use only.
     */
    public void setJoint(int j, Joint joint)
    {
        joints[j] = joint;
    }

    /**
     * For internal use only.
     */
    public void setImage(GreenfootImage img, int x, int y)
    {
        this.img = img;
        this.imgX = x;
        this.imgY = y;
    }    
    
    /**
     * Gets the identifer of this user.  This is an arbitrary number.  It is guaranteed
     * to be unique among the current set of users, but if a user leaves the scene,
     * or tracking fails, the identifier may be re-used again in future.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns true if the user is being tracked.
     * 
     * Tracked means that they have been calibrated by the sensor, and we are now receiving
     * updates on the positions of their joints.
     */
    public boolean isTracking()
    {
        return state == 2;
    }
    
    /**
     * Returns true if the user is currently calibrating.
     * 
     * Calibrating means they have assumed the arms-up position, and the sensor is spending a few
     * seconds trying to calibrate.  If this is successful, they will move to the tracking
     * state, after which this method will return false.  If it is eventually unsuccessful they will return
     * to the default state.
     */
    public boolean isCalibrating()
    {
        return state == 1;
    }
    
    /**
     * Returns true if isTracking() currently returns true, but would have returned false before the last
     * update from the server.
     * 
     * This indicates that this frame is the first one in which they are being tracked, having
     * been calibrating during the previous frame.
     */
    public boolean startedTracking()
    {
        return state == 2 && previousState != state;
    }
    
    /**
     * Returns true if isCalibrating() currently returns true, but would have returned false before the last
     * update from the server.
     * 
     * This indicates that this frame is the first one in which they are being calibrated, having
     * been in the default state during the previous frame.
     */
    public boolean startedCalibrating()
    {
        return state == 1 && previousState != state;
    }

    /**
     * Gets the current user outline image.  This will be a cutout of the user,
     * which will be transparent wherever the user is not detected.  So for example,
     * if they stand with their legs apart, it will be transparent between their legs.
     *
     * The image will not (generally) be the same size as the world; it will only
     * be large enough to contain the user's image.  Use getImageX() and getImageY()
     * to find out where the user's image resides in relation to the world as a whole.
     */
    public GreenfootImage getImage()
    {
        return img;
    }
    
    /**
     * The left of the image returned by getImage().
     */
    public int getImageX()
    {
        return imgX;
    }
    
    /**
     * The top of the image returned by getImage().
     */
    public int getImageY()
    {
        return imgY;
    }
    
    /**
     * Gets the joint associated with a given index.  This is identical to getAllJoints()[index].  Valid
     * values to pass in are:
     * 
     * Joint.HEAD
     * Joint.NECK
     * Joint.TORSO
     * Joint.LEFT_SHOULDER
     * Joint.LEFT_ELBOW
     * Joint.LEFT_HAND
     * Joint.RIGHT_SHOULDER
     * Joint.RIGHT_ELBOW
     * Joint.RIGHT_HAND
     * Joint.LEFT_HIP
     * Joint.LEFT_KNEE
     * Joint.LEFT_FOOT
     * Joint.RIGHT_HIP
     * Joint.RIGHT_KNEE
     * Joint.RIGHT_FOOT
     * 
     * Joint data is only valid if isTracking() currently returns true.
     */
    public Joint getJoint(int index)
    {
        return joints[index];
    }
    
    /**
     * Gets the position of all the joints for the user.  The array will be of size Joint.NUM_JOINTS,
     * and the different joints can be accessed using the following indexes from Joint:
     * 
     * Joint.HEAD
     * Joint.NECK
     * Joint.TORSO
     * Joint.LEFT_SHOULDER
     * Joint.LEFT_ELBOW
     * Joint.LEFT_HAND
     * Joint.RIGHT_SHOULDER
     * Joint.RIGHT_ELBOW
     * Joint.RIGHT_HAND
     * Joint.LEFT_HIP
     * Joint.LEFT_KNEE
     * Joint.LEFT_FOOT
     * Joint.RIGHT_HIP
     * Joint.RIGHT_KNEE
     * Joint.RIGHT_FOOT
     * 
     * Joint data is only valid if isTracking() currently returns true.
     */
    public Joint[] getAllJoints()
    {
        //Shallow copy of our array:
        Joint[] jointsCopy = new Joint[joints.length];
        System.arraycopy(joints, 0, jointsCopy, 0, joints.length);
        return jointsCopy;
    }
    
    /**
     * Gets the index of the joint that is nearest to the sensor.  So for example, if the user
     * holds their right hand out towards the sensor, this will return Joint.RIGHT_HAND.
     * 
     * Note that if you spin your arm around in a circle, it is quite possible when your arm
     * is across your body that your elbow is further forward than your hand, at which point
     * Joint.RIGHT_ELBOW might be returned instead.
     * 
     * This return value is only valid if isTracking() currently returns true.
     */
    public int getNearestJoint()
    {
        return Joint.minJointBy(joints, Joint.compareZ());
    }
    
    /**
     * Gets the index of the joint that is vertically highest.  For a normal standing position
     * this will typically be Joint.HEAD, but if you raise your arm it would be Joint.LEFT_HAND
     * or JOINT.RIGHT_HAND.
     * 
     * This return value is only valid if isTracking() currently returns true.
     */
    public int getHighestJoint()
    {
        return Joint.minJointBy(joints, Joint.compareY());
    }
    
    /**
     * Draws the user's joint data as a stick figure onto the given image.  The image
     * should be of the right size to contain the world.  The second parameter is
     * the radius to draw the head circle (this can't be inferred from the joints).
     * 60 is an okay value when using a 640*480 world, you may want to adjust it to your preference.
     */
    public void drawStickFigure(GreenfootImage img, int headRadius)
    {

        img.setColor(java.awt.Color.RED);
        //img.setStroke(new BasicStroke(3f));
        connect(img, Joint.HEAD, Joint.NECK);
        img.drawOval((int)joints[Joint.HEAD].getScreenPosition().getX() - (headRadius/2),
                     (int)joints[Joint.HEAD].getScreenPosition().getY() - (headRadius/2),
                     headRadius, headRadius);
        connect(img, Joint.NECK, Joint.TORSO);
        Point3D hipMidPoint = joints[Joint.LEFT_HIP].getScreenPosition().midPoint(joints[Joint.RIGHT_HIP].getScreenPosition());
        img.drawLine((int)joints[Joint.TORSO].getScreenPosition().getX(),
                     (int)joints[Joint.TORSO].getScreenPosition().getY(),
                     (int)hipMidPoint.getX(),
                     (int)hipMidPoint.getY());
        connect(img, Joint.LEFT_HIP, Joint.RIGHT_HIP);
        for (int side : new int[] {Joint.LEFT, Joint.RIGHT})
        {
            connect(img, Joint.NECK, side+Joint.SHOULDER);
            connect(img, side+Joint.SHOULDER, side+Joint.ELBOW);
            connect(img, side+Joint.ELBOW, side+Joint.HAND);
            
            connect(img, side+Joint.HIP, side+Joint.KNEE);
            connect(img, side+Joint.KNEE, side+Joint.FOOT);
        }
    }
    
    private void connect(GreenfootImage img, int jA, int jB)
    {
        img.drawLine((int)joints[jA].getScreenPosition().getX(),
                     (int)joints[jA].getScreenPosition().getY(),
                     (int)joints[jB].getScreenPosition().getX(),
                     (int)joints[jB].getScreenPosition().getY());
    }
    

    /**
     * Makes a scaled copy of this UserData where the image and all the
     * screen positions for joints are scaled by the given factor.
     *
     * If you pass 1.0f as the scale, it will return this, not a copy.
     */
    public UserData scaledCopy(float scale)
    {
        if (scale == 1.0f) return this;
        else return new UserData(this, scale);
    }
}
