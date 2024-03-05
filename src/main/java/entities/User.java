package entities;

public class User {

    public String nom;
    public String prenom;
    public int age ,id ;
    public String email;
    public String photoProfile;
    public String password;

    public String phoneNumber;

    public String otp ;
   public int isCoach , isAdmin ;


    public User() {

    }

    public User(String nom,  String prenom , int age,  String email, String photoProfile, String password, String phoneNumber) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoProfile = photoProfile;
        this.age = age ;
    }

    public User (String otp) {
        this.otp = otp ;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getIsCoach() {
        return isCoach;
    }

    public void setIsCoach(int isCoach) {
        this.isCoach = isCoach;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public User(String nom, String prenom, int age, int id, String email, String photoProfile, String password, String phoneNumber, String otp, int isCoach, int isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.id = id;
        this.email = email;
        this.photoProfile = photoProfile;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.otp = otp;
        this.isCoach = isCoach;
        this.isAdmin = isAdmin;
    }

    public User(String nom, String prenom , int age, String email, String photoProfile, String password, String phoneNumber , int isCoach , int isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isCoach = isCoach ;
        this.isAdmin = isAdmin ;
        this.photoProfile=photoProfile;
        this.age= age ;

    }
    public User(String nom,  String prenom , int age,  String email, String photoProfile, String password, String phoneNumber , int isCoach , int isAdmin , int id) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isCoach = isCoach ;
        this.isAdmin = isAdmin ;
        this.photoProfile=photoProfile;
        this.age= age ;
        this.id=id;

    }

    public int isCoach() {
        return isCoach;
    }

    public void setCoach(int coach) {
        isCoach = coach;
    }

    public int isAdmin() {
        return isAdmin;
    }

    public void setAdmin(int admin) {
        isAdmin = admin;
    }


    public User(int id , String nom, String prenom , int age, String email, String photoProfile, String password, String phoneNumber) {
        this.id = id ;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoProfile = photoProfile ;
        this.age = age ;
    }

    public int getId(){
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id){
        this.id = id ;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", photoProfile='" + photoProfile + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", isCoach=" + isCoach +
                ", isAdmin=" + isAdmin +
                '}';
    }
}