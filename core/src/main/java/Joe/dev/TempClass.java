package Joe.dev;

public class TempClass {
    private float Temp;

    TempClass(){
        Temp = 2;
    }

    public float CheckTemp(){
        return Temp;
    }

    public float getTemp(float dlt) {
        if (Temp > 2) {
            Temp -= dlt;
            return 2;
        }
        Temp -= dlt;
        return Temp;
    }

    public void setTemp(float dlt) {
        Temp = dlt;
    }
}
