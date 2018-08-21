package deepak.com.perpulesound.db;

import org.parceler.Parcel;

import io.realm.RealmObject;

@Parcel(Parcel.Serialization.BEAN)
public class RealmData extends RealmObject {

    private String itemId;
    private String desc;
    private String audio;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}