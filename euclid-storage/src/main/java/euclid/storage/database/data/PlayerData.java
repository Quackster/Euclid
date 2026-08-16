package euclid.storage.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class PlayerData implements IEntityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "figure")
    private String figure;

    @Column(name = "direct_mail")
    private boolean directMail;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "custom_data")
    private String customData;

    @Column(name = "sex")
    private String sex;

    @Column(name = "country")
    private String country;

    @Column(name = "join_date", insertable = false)
    private LocalDateTime joinDate;

    @Column(name = "last_online", insertable = false)
    private LocalDateTime lastOnline;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getFigure() {
        return figure;
    }

    @Override
    public void setFigure(String figure) {
        this.figure = figure;
    }

    public boolean isDirectMail() {
        return directMail;
    }

    public void setDirectMail(boolean directMail) {
        this.directMail = directMail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getCustomData() {
        return customData;
    }

    @Override
    public void setCustomData(String customData) {
        this.customData = customData;
    }

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(LocalDateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Transient
    public int getRank() {
        return 1;
    }
}
