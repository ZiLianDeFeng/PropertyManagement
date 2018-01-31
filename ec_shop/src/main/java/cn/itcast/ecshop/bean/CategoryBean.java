package cn.itcast.ecshop.bean;

/**
 * Created by wanjiawen on 2016/8/10.
 */
public  class CategoryBean {
    private int id;
    private boolean isLeafNode;
    private String name;
    private int parentId;
    private String pic;
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsLeafNode() {
        return isLeafNode;
    }

    public void setIsLeafNode(boolean isLeafNode) {
        this.isLeafNode = isLeafNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "id=" + id +
                ", isLeafNode=" + isLeafNode +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", pic='" + pic + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
