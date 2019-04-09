package Dtos;


public class AccessControlDto
{
    public AccessControlDto(boolean accessibility,String path)
    {
        this.accessibility=accessibility;
        this.path=path;
    }

    private boolean accessibility;
    public boolean getAccessibility(){
        return accessibility;
    }

    public void setAccessibility(boolean value) {
        accessibility=value;
    }

    private String path;
    public String getPath(){
        return path;
    }

    public void setPath(String value) {
        path=value;
    }

}