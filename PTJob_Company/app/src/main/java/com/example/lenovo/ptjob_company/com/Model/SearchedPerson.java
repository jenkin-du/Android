package com.example.lenovo.ptjob_company.com.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**联系人
 * Created by Administrator on 2016/8/29.
 */
public class SearchedPerson implements Parcelable{

    private String pId;
    private String imageName;
    private String name;
    private String school;


    protected SearchedPerson(Parcel in) {
        pId = in.readString();
        imageName = in.readString();
        name = in.readString();
        school = in.readString();
    }

    public static final Creator<SearchedPerson> CREATOR = new Creator<SearchedPerson>() {
        @Override
        public SearchedPerson createFromParcel(Parcel in) {
            return new SearchedPerson(in);
        }

        @Override
        public SearchedPerson[] newArray(int size) {
            return new SearchedPerson[size];
        }
    };

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "SearchedPerson{" +
                "pId='" + pId + '\'' +
                ", imageName='" + imageName + '\'' +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        return this.pId.equals(((SearchedPerson)o).pId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pId);
        dest.writeString(imageName);
        dest.writeString(name);
        dest.writeString(school);
    }
}
