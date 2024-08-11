package chire.val.tutorial.camera;

public class Vector2 {
    public float x=0, y=0;

    public Vector2(){
    }

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 operator(Vector2 vec) {
        return new Vector2(x+vec.x, y+vec.y);
    }

    //此处省略一iiiiiii堆东西

    public float length(){
        return (float) Math.sqrt(x*x+y*y);
    }

    public Vector2 normalize(){
        float len = length();

        if (len == 0) return new Vector2(0, 0);

        return new Vector2(x/len, y/len);
    }
}
