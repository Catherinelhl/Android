package bcaasc.io.chartdemo.constants;


/**
 * @author catherine.brainwilliam
 * @since 2018/11/12
 * <p>
 * 定义一般常量
 */
public class Constants {

    public static final String ENCODE_IGNORE_CASE = "identity";//http設置encode忽略
    public static final long SleepTime800 = 800;
    public static final String APIKEY = "";

    /*cycle time to choose*/
    public enum CycleTime {
        oneDay("1D"),
        sevenDay("7D"),
        oneMonth("1M"),
        threeMonth("3M"),
        oneYear("1Y"),
        YTD("YTD"),
        ALL("ALL");

        CycleTime(String s) {
            this.name = s;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
