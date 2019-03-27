package cn.pumpkin.angrypandawebview.identity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: zhibao.Liu
 * @version:
 * @date: 2019/3/26 16:44
 * @des: 传递二代身份证数据体
 * @see {@link }
 */

public class IdentityParcel implements Parcelable {

    private String name;
    private String sex;
    private String nation;
    private String birthday;
    private String address;
    private String cardNo;
    private String grantDept;
    private String userLifeBegin;
    private String userLifeEnd;
    private String featureFp;
    private String picPath;

    protected IdentityParcel(Parcel in) {
        name = in.readString();
        sex = in.readString();
        nation = in.readString();
        birthday = in.readString();
        address = in.readString();
        cardNo = in.readString();
        grantDept = in.readString();
        userLifeBegin = in.readString();
        userLifeEnd = in.readString();
        featureFp = in.readString();
        picPath = in.readString();
    }

    public IdentityParcel() {

    }

    public IdentityParcel(String name, String sex, String nation, String birthday, String address,
                          String cardNo, String grantDept, String userLifeBegin, String userLifeEnd,
                          String feature,String pic) {
        this.name = name;
        this.sex = sex;
        this.nation = nation;
        this.birthday = birthday;
        this.address = address;
        this.cardNo = cardNo;
        this.grantDept = grantDept;
        this.userLifeBegin = userLifeBegin;
        this.userLifeEnd = userLifeEnd;
        this.featureFp = feature;
        this.picPath = pic;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(nation);
        dest.writeString(birthday);
        dest.writeString(address);
        dest.writeString(cardNo);
        dest.writeString(grantDept);
        dest.writeString(userLifeBegin);
        dest.writeString(userLifeEnd);
        dest.writeString(featureFp);
        dest.writeString(picPath);
    }

    public static final Creator<IdentityParcel> CREATOR = new Creator<IdentityParcel>() {
        @Override
        public IdentityParcel createFromParcel(Parcel in) {
            return new IdentityParcel(in);
        }

        @Override
        public IdentityParcel[] newArray(int size) {
            return new IdentityParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getGrantDept() {
        return grantDept;
    }

    public void setGrantDept(String grantDept) {
        this.grantDept = grantDept;
    }

    public String getUserLifeBegin() {
        return userLifeBegin;
    }

    public void setUserLifeBegin(String userLifeBegin) {
        this.userLifeBegin = userLifeBegin;
    }

    public String getUserLifeEnd() {
        return userLifeEnd;
    }

    public void setUserLifeEnd(String userLifeEnd) {
        this.userLifeEnd = userLifeEnd;
    }

    public String getFeatureFp() {
        return featureFp;
    }

    public void setFeatureFp(String featureFp) {
        this.featureFp = featureFp;
    }

    public static Creator<IdentityParcel> getCREATOR() {
        return CREATOR;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public String toString() {
        return "IdentityParcel{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", grantDept='" + grantDept + '\'' +
                ", userLifeBegin='" + userLifeBegin + '\'' +
                ", userLifeEnd='" + userLifeEnd + '\'' +
                ", featureFp='" + featureFp + '\'' +
                ", picPath='" + picPath + '\'' +
                '}';
    }
}
