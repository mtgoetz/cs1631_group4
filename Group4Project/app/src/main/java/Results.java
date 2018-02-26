import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mthom on 2/25/2018.
 */

public class Results implements Parcelable {
    protected Results(Parcel in) {
    }

    public static final Creator<Results> CREATOR = new Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }


}
