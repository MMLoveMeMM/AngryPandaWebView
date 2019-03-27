package cn.pumpkin.angrypandawebview.identity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
/**
 * @author: zhibao.Liu
 * @version:
 * @date: 2019/3/26 16:44
 * @des: 传递外国人居住证数据体
 * @see {@link }
 */

public class ForeignParcel implements Parcelable {

    private String foreignName;
    private String chinaName;
    private String sex;
    private String nation;
    private String countryCode;
    private String birthday;
    private String address;
    private String cardNo;
    private String IDVer;
    private String userLifeBegin;
    private String userLifeEnd;

    protected ForeignParcel(Parcel in) {
        foreignName = in.readString();
        chinaName = in.readString();
        sex = in.readString();
        nation = in.readString();
        countryCode = in.readString();
        birthday = in.readString();
        address = in.readString();
        cardNo = in.readString();
        IDVer = in.readString();
        userLifeBegin = in.readString();
        userLifeEnd = in.readString();
    }

    public ForeignParcel() {

    }

    public ForeignParcel(String foreignName,String chinaName, String sex, String nation,String countryCode, String birthday, String address,
                          String cardNo, String IDVer, String userLifeBegin, String userLifeEnd) {
        this.foreignName = foreignName;
        this.chinaName = chinaName;
        this.sex = sex;
        this.nation = nation;
        this.countryCode = countryCode;
        this.birthday = birthday;
        this.address = address;
        this.cardNo = cardNo;
        this.IDVer = IDVer;
        this.userLifeBegin = userLifeBegin;
        this.userLifeEnd = userLifeEnd;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(foreignName);
        dest.writeString(chinaName);
        dest.writeString(sex);
        dest.writeString(nation);
        dest.writeString(countryCode);
        dest.writeString(birthday);
        dest.writeString(address);
        dest.writeString(cardNo);
        dest.writeString(IDVer);
        dest.writeString(userLifeBegin);
        dest.writeString(userLifeEnd);
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

        
    public String getChinaName() {
		return chinaName;
	}

	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}

	public String getForeignName() {
		return foreignName;
	}

	public void setForeignName(String foreignName) {
		this.foreignName = foreignName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIDVer() {
		return IDVer;
	}

	public void setIDVer(String iDVer) {
		IDVer = iDVer;
	}

	public String getName() {
        return foreignName;
    }

    public void setName(String foreignName) {
        this.foreignName = foreignName;
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

    public static Creator<IdentityParcel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "IdentityParcel{" +
                "foreignName='" + foreignName + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", userLifeBegin='" + userLifeBegin + '\'' +
                ", userLifeEnd='" + userLifeEnd + '\'' +
                '}';
    }
}
