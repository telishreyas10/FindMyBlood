package com.blizzard.app.findmyblood;

public class Users {



    private String Uid;
   private String Address;
   private String FullName;
   private String Location;
   private String Bloodgroup;
   private String Age;
   private String Phone;
   private String image;
   public Users(){


   }

   public Users(String uid, String address, String fullName, String location, String bloodgroup, String age, String phone, String image) {
      Uid = uid;
      Address = address;
      FullName = fullName;
      Location = location;
      Bloodgroup = bloodgroup;
      Age = age;
      Phone = phone;
      this.image = image;
   }

   public String getPhone() {
      return Phone;
   }

   public void setPhone(String phone) {
      Phone = phone;
   }



   public String getAddress() {
      return Address;
   }

   public void setAddress(String address) {
      Address = address;
   }

   public String getFullName() {
      return FullName;
   }

   public void setFullName(String fullName) {
      FullName = fullName;
   }

   public String getLocation() {
      return Location;
   }

   public void setLocation(String location) {
      Location = location;
   }

   public String getBloodgroup() {
      return Bloodgroup;
   }

   public void setBloodgroup(String bloodgroup) {
      Bloodgroup = bloodgroup;
   }

   public String getAge() {
      return Age;
   }

   public void setAge(String age) {
      Age = age;
   }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }
}
