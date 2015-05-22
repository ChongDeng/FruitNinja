/**
 * A simple three-dimensional point class.
 */
public class Point3D
{
    private final float x;
    private final float y;
    private final float z;
    
    public Point3D(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public float getZ()
    {
        return z;
    }

    public String toString()
    {
        return "(" + x + "," + y + "," + z + ")";
    }
    
    /**
     * Gets a new point that is halfway between this point and the given parameter.
     */
    public Point3D midPoint(Point3D o)
    {
        return new Point3D((this.x + o.x) / 2, (this.y + o.y) / 2, (this.z + o.z) / 2);
    }
    
    /**
     * Gets a copy of this point with x, y and z scaled by the given factor.
     */
    public Point3D scaledCopy(float scale)
    {
        return new Point3D(x * scale, y * scale, z * scale);
    }
}
