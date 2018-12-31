package bit.xchangecrypt.client.datamodel;

import bit.xchangecrypt.client.core.Constants;

import java.io.Serializable;

/**
 * Created by V3502484 on 21. 9. 2016.
 */
public abstract class BaseObject implements Serializable, Constants {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BaseObject(int id) {
        this.id = id;
    }

    public BaseObject() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseObject)) return false;

        BaseObject that = (BaseObject) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
