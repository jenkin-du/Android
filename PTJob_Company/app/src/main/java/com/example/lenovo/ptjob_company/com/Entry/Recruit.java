package com.example.lenovo.ptjob_company.com.Entry;

import android.os.Parcel;
import android.os.Parcelable;

public class Recruit implements Parcelable{

	private String infoId;
	private String PluralistId;
	private String applyReason;
	private String applyStatus;

	protected Recruit(Parcel in) {
		infoId = in.readString();
		PluralistId = in.readString();
		applyReason = in.readString();
		applyStatus = in.readString();
	}

	public static final Creator<Recruit> CREATOR = new Creator<Recruit>() {
		@Override
		public Recruit createFromParcel(Parcel in) {
			return new Recruit(in);
		}

		@Override
		public Recruit[] newArray(int size) {
			return new Recruit[size];
		}
	};

	public String getInfoId() {
		return infoId;
	}
	public String getPluralistId() {
		return PluralistId;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public void setPluralistId(String pluralistId) {
		PluralistId = pluralistId;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	
	@Override
	public String toString() {
		return "Recruit [infoId=" + infoId + ", PluralistId=" + PluralistId + ", applyReason=" + applyReason
				+ ", applyStatus=" + applyStatus + "]";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(infoId);
		dest.writeString(PluralistId);
		dest.writeString(applyReason);
		dest.writeString(applyStatus);
	}
}
