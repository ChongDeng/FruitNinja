import java.util.Comparator;

/**
 * Data for a joint in a skeleton for a tracked user.
 */
public class Joint
{
    /**
     * The total number of joints that are tracked by the sensor.
     */
    public static final int NUM_JOINTS = 15;

    /**
     * The head joint (centre of the head).
     */
    public static final int HEAD = 0;
    /**
     * The neck joint (this seems to be fixed as the midpoint of the two shoulders)
     */
    public static final int NECK = 1;
    /**
     * The torso "joint" (this seems to be fixed as the midpoint of the two shoulders and two hips)
     */
    public static final int TORSO = 2;
    
    /**
     * The offset for LEFT joints.  You can use this and the constants RIGHT,
     * SHOULDER, ELBOW, HAND, HIP, KNEE and FOOT to process both sides with one piece of code.
     * For example, here is how we draw the stick figure:
     * 
     * for (int side : new int[] {Joint.LEFT, Joint.RIGHT})
       {
            connect(img, Joint.NECK, side + Joint.SHOULDER);
            connect(img, side + Joint.SHOULDER, side + Joint.ELBOW);
            connect(img, side + Joint.ELBOW, side + Joint.HAND);
            
            connect(img, side + Joint.HIP, side + Joint.KNEE);
            connect(img, side + Joint.KNEE, side + Joint.FOOT);
       }
     * 
     * If that doesn't make any sense, or you don't need this, you might want to use
     * the constants LEFT_HAND, RIGHT_KNEE etc.
     */
    public static final int LEFT = 0;
    public static final int RIGHT = 3;
    public static final int SHOULDER = 3;
    public static final int ELBOW = 4;
    public static final int HAND = 5;
    public static final int HIP = 9;
    public static final int KNEE = 10;
    public static final int FOOT = 11;

    /**
     * The left shoulder.  Note that this is your actual left shoulder, but because
     * the display in Kinect scenarios is always a mirror image of yourself, this
     * will appear on the right-hand side of the screen.  The same goes for all the other joints.
     */
    public static final int LEFT_SHOULDER = LEFT+SHOULDER;
    public static final int LEFT_ELBOW = LEFT+ELBOW;
    public static final int LEFT_HAND = LEFT+HAND;
    public static final int LEFT_HIP = LEFT+HIP;
    public static final int LEFT_KNEE = LEFT+KNEE;
    public static final int LEFT_FOOT = LEFT+FOOT;

    public static final int RIGHT_SHOULDER = RIGHT+SHOULDER;
    public static final int RIGHT_ELBOW = RIGHT+ELBOW;
    public static final int RIGHT_HAND = RIGHT+HAND;
    public static final int RIGHT_HIP = RIGHT+HIP;
    public static final int RIGHT_KNEE = RIGHT+KNEE;
    public static final int RIGHT_FOOT = RIGHT+FOOT;
    
    
    private final int joint;
    private final float confidence;
    private final Point3D positionWorld;
    private final Point3D positionScreen;
    
    /**
     * For internal use only.
     */
    public Joint(int joint, float confidence, Point3D positionWorld, Point3D positionScreen)
    {
        this.joint = joint;
        this.confidence = confidence;
        this.positionWorld = positionWorld;
        this.positionScreen = positionScreen;
    }
    
    /**
     * Gets the joint index for this joint (e.g. Joint.HEAD or JOINT.LEFT_HAND)
     */
    public int getJointIndex()
    {
        return joint;
    }
    
    /**
     * Gets the confidence value associated with a joint (a number between 0 and 1, where 0 is least confidence and 1 is most confidence).
     * 
     * The Kinect sensor indicates its confidence in a joint's position.  If you move behind a table,
     * the confidence on your now-obscured knees and feet will drop accordingly.
     * 
     * Currently, all the methods (e.g. minJointBy) ignore this value, so if you want to use it
     * you'll need to write the extra code yourself.  It seems that it is also not supported
     * properly by OpenNI at the moment.
     */
    public float getConfidence()
    {
        return confidence;
    }
    
    /**
     * Gets the X coordinate of this joint on the screen.
     */
    public int getX()
    {
        return Math.round(getScreenPosition().getX());
    }
    
    /**
     * Gets the Y coordinate of this joint on the screen.
     */
    public int getY()
    {
        return Math.round(getScreenPosition().getY());
    }
    
    /**
     * Gets the position of this joint on screen as a 3D point.
     * 
     * The X and Y are as usual, and the Z is a depth coordinate which you can use to measure
     * how close the joint is to the sensor (lower values are nearer the sensor).
     */
    public Point3D getScreenPosition()
    {
        return positionScreen;
    }

    /**
     * Gets the physical position of the joint in the world as a 3D point.
     */    
    public Point3D getPhysicalPosition()
    {
        return positionWorld;
    }

    public String toString()
    {
        return positionScreen.toString();
    }

    /**
     * Gets the minimum joint according to the given comparator.  You can
     * use this with the methods compareX(), compareY() and compareZ() to find the most extreme
     * joints in a particular dimension.
     * 
     * For example, minJointBy(userData.getAllJoints(), compareX()) will get the leftmost joint
     * (as it appears on screen - this will probably be your right hand when you face the sensor).
     * minJointBy(userData.getAllJoints(), compareY()) will get the highest joint
     * (Y coordinates get larger as you go downwards, as on screen, but contrary to mathematical convention).
     * minJointBy(userData.getAllJoints(), compareZ()) will get the nearest joint to the sensor.
     * 
     * The UserData.getNearestJoint() and UserData.getHighestJoint() are useful shorthands for
     * using this method on Z and Y respectively.
     * 
     * Note that this method completely ignores the confidence value associated with each joint.
     */
    public static int minJointBy(Joint[] joints, Comparator<Joint> cmp)
    {
        Joint[] jointCopy = new Joint[joints.length];
        System.arraycopy(joints, 0, jointCopy, 0, joints.length);
        java.util.Arrays.sort(jointCopy, cmp);
        return jointCopy[0].joint;
    }
    
    /**
     * Gets the maximum joint according to the given comparator.  You can
     * use this with the methods compareX(), compareY() and compareZ() to find the most extreme
     * joints in a particular dimension.
     * 
     * For example, maxJointBy(userData.getAllJoints(), compareX()) will get the rightmost joint
     * (as it appears on screen - this will probably be your left hand when you face the sensor).
     * maxJointBy(userData.getAllJoints(), compareY()) will get the lowest joint
     * (Y coordinates get larger as you go downwards, as on screen, but contrary to mathematical convention).
     * maxJointBy(userData.getAllJoints(), compareZ()) will get the furthers joint from the sensor.
     * 
     * Note that this method completely ignores the confidence value associated with each joint.
     */
    public static int maxJointBy(Joint[] joints, Comparator<Joint> cmp)
    {
        Joint[] jointCopy = new Joint[joints.length];
        System.arraycopy(joints, 0, jointCopy, 0, joints.length);
        java.util.Arrays.sort(jointCopy, cmp);
        return jointCopy[jointCopy.length - 1].joint;
    }
    
    public static Comparator<Joint> compareX()
    {
        return new XComparator();
    }
    
    public static Comparator<Joint> compareY()
    {
        return new YComparator();
    }
    
    public static Comparator<Joint> compareZ()
    {
        return new ZComparator();
    }
    
    private static class XComparator implements Comparator<Joint>
    {
        public int compare(Joint a, Joint b)
        {
            Joint aj = (Joint)a;
            Joint bj = (Joint)b;
            return Float.compare (aj.positionScreen.getX(), bj.positionScreen.getX());
        }
    }
    
    private static class YComparator implements Comparator<Joint>
    {
        public int compare(Joint a, Joint b)
        {
            Joint aj = (Joint)a;
            Joint bj = (Joint)b;
            return Float.compare (aj.positionScreen.getY(), bj.positionScreen.getY());
        }
    }
    
    private static class ZComparator implements Comparator<Joint>
    {
        public int compare(Joint a, Joint b)
        {
            Joint aj = (Joint)a;
            Joint bj = (Joint)b;
            return Float.compare (aj.positionScreen.getZ(), bj.positionScreen.getZ());
        }
    }

    /**
     * Gets a scaled copy of the Joint where all the screen positions are scaled by the given factor.
     */
    public Joint scaledCopy(float scale)
    {
        return new Joint(joint, confidence, positionWorld, positionScreen.scaledCopy(scale));
    }
}
