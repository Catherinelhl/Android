package bcaasc.io.chartdemo.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/23
 * CoinMarketCap
 */
public class LineBean implements Serializable {
    private String x;
    private String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LineBean{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
