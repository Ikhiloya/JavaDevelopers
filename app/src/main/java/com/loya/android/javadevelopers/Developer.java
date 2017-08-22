package com.loya.android.javadevelopers;


/**
 * A custom java class to store Developer data
 */
public class Developer {
    //username of the developer
    private String mUserName;

    //the image url of the developer
    private String mImageUrl;

    //url of the user profile
    private String mProfileUrl;


    /**
     * A constructor for the custom Developer class
     *
     * @param userName   is the name of the java developer on github
     * @param imageUrl   is the url of the imagge(avatar) of the java developer on github
     * @param profileUrl is the url to the profile of the java developer on github
     */
    public Developer(String userName, String imageUrl, String profileUrl) {
        this.mUserName = userName;
        this.mImageUrl = imageUrl;
        this.mProfileUrl = profileUrl;
    }


    /**
     * @returns the username of the java developer
     */
    public String getmUserName() {
        return mUserName;
    }

    /**
     * @returns the image url of the java developer
     */
    public String getmImageUrl() {
        return mImageUrl;
    }


    /**
     * @returns the profile url of the java developer
     */
    public String getmProfileUrl() {
        return mProfileUrl;
    }
}
