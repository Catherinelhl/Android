package bcaasc.io.chartdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/5
 * 币种的列表
 */
public class ListOfCoinMarketCap implements Serializable {
    private List<Data> data;
    private Metadata metadata;

    class Data {
        private long id;
        private String name;
        private String symbol;
        private String website_slug;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getWebsite_slug() {
            return website_slug;
        }

        public void setWebsite_slug(String website_slug) {
            this.website_slug = website_slug;
        }

        @Override
        public String toString() {
            return "data{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", website_slug='" + website_slug + '\'' +
                    '}';
        }
    }

    class Metadata {
        private long timestamp;
        private long num_cryptocurrencies;
        private String error;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getNum_cryptocurrencies() {
            return num_cryptocurrencies;
        }

        public void setNum_cryptocurrencies(long num_cryptocurrencies) {
            this.num_cryptocurrencies = num_cryptocurrencies;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return "Metadata{" +
                    "timestamp=" + timestamp +
                    ", num_cryptocurrencies=" + num_cryptocurrencies +
                    ", error='" + error + '\'' +
                    '}';
        }
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ListOfCoinMarketCap{" +
                "data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}
