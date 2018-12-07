package bcaasc.io.chartdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/7
 * 完整的市場幣種信息
 */
public class CompleteCurrencyBean implements Serializable {
    private Status status;
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

 protected    class Status {
        private String timestamp;
        private int error_code;
        private String error_message;
        private int elapsed;
        private int credit_count;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public int getError_code() {
            return error_code;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }

        public String getError_message() {
            return error_message;
        }

        public void setError_message(String error_message) {
            this.error_message = error_message;
        }

        public int getElapsed() {
            return elapsed;
        }

        public void setElapsed(int elapsed) {
            this.elapsed = elapsed;
        }

        public int getCredit_count() {
            return credit_count;
        }

        public void setCredit_count(int credit_count) {
            this.credit_count = credit_count;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "timestamp='" + timestamp + '\'' +
                    ", error_code=" + error_code +
                    ", error_message='" + error_message + '\'' +
                    ", elapsed=" + elapsed +
                    ", credit_count=" + credit_count +
                    '}';
        }
    }

   public class Data {
        private long id;// id
        private String name;// 名称



        private String symbol; // 符号
        private String slug;// 用于获取该货币详细信息的参数
        private float circulating_supply; // 当前流通的大概硬币数量
        private float total_supply;//现在存在的大致硬币总量
        private float max_supply;// 货币生命周期中存在的最大硬币数量的最佳近似值
        private String date_added;
        private float num_market_pairs;//交易每种货币的所有交易所的市场对数
        private List<String> tags;
        private float cmc_rank;
        private String last_updated;
        private Quote quote;

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

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public float getCirculating_supply() {
            return circulating_supply;
        }

        public void setCirculating_supply(float circulating_supply) {
            this.circulating_supply = circulating_supply;
        }

        public float getTotal_supply() {
            return total_supply;
        }

        public void setTotal_supply(float total_supply) {
            this.total_supply = total_supply;
        }

        public float getMax_supply() {
            return max_supply;
        }

        public void setMax_supply(float max_supply) {
            this.max_supply = max_supply;
        }

        public String getDate_added() {
            return date_added;
        }

        public void setDate_added(String date_added) {
            this.date_added = date_added;
        }

        public float getNum_market_pairs() {
            return num_market_pairs;
        }

        public void setNum_market_pairs(float num_market_pairs) {
            this.num_market_pairs = num_market_pairs;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }


        public float getCmc_rank() {
            return cmc_rank;
        }

        public void setCmc_rank(float cmc_rank) {
            this.cmc_rank = cmc_rank;
        }

        public String getLast_updated() {
            return last_updated;
        }

        public void setLast_updated(String last_updated) {
            this.last_updated = last_updated;
        }

        public Quote getQuote() {
            return quote;
        }

        public void setQuote(Quote quote) {
            this.quote = quote;
        }

       public class Quote {
            private USD USD;

            public Quote.USD getUSD() {
                return USD;
            }

            public void setUSD(Quote.USD USD) {
                this.USD = USD;
            }

          public   class USD {
                private float price;//当前最新单价
                private float volume_24h;// 货币的24小时交易量
                private float percent_change_1h; //货币的1小时交易价格百分比变化
                private float percent_change_24h;//货币的24小时交易价格百分比变化
                private float percent_change_7d;//货币的7天交易价格百分比变化
                private float market_cap; // 最新总市值
                private String last_updated;

                @Override
                public String toString() {
                    return "USD{" +
                            "price=" + price +
                            ", volume_24h=" + volume_24h +
                            ", percent_change_1h=" + percent_change_1h +
                            ", percent_change_24h=" + percent_change_24h +
                            ", percent_change_7d=" + percent_change_7d +
                            ", market_cap=" + market_cap +
                            ", last_updated='" + last_updated + '\'' +
                            '}';
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public float getVolume_24h() {
                    return volume_24h;
                }

                public void setVolume_24h(float volume_24h) {
                    this.volume_24h = volume_24h;
                }

                public float getPercent_change_1h() {
                    return percent_change_1h;
                }

                public void setPercent_change_1h(float percent_change_1h) {
                    this.percent_change_1h = percent_change_1h;
                }

                public float getPercent_change_24h() {
                    return percent_change_24h;
                }

                public void setPercent_change_24h(float percent_change_24h) {
                    this.percent_change_24h = percent_change_24h;
                }

                public float getPercent_change_7d() {
                    return percent_change_7d;
                }

                public void setPercent_change_7d(float percent_change_7d) {
                    this.percent_change_7d = percent_change_7d;
                }

                public float getMarket_cap() {
                    return market_cap;
                }

                public void setMarket_cap(float market_cap) {
                    this.market_cap = market_cap;
                }

                public String getLast_updated() {
                    return last_updated;
                }

                public void setLast_updated(String last_updated) {
                    this.last_updated = last_updated;
                }
            }

            @Override
            public String toString() {
                return "Quote{" +
                        "USD=" + USD +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", slug='" + slug + '\'' +
                    ", circulating_supply=" + circulating_supply +
                    ", total_supply=" + total_supply +
                    ", max_supply=" + max_supply +
                    ", date_added='" + date_added + '\'' +
                    ", num_market_pairs=" + num_market_pairs +
                    ", tags=" + tags +
                    ", cmc_rank=" + cmc_rank +
                    ", last_updated='" + last_updated + '\'' +
                    ", quote=" + quote +
                    '}';
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CompleteCurrencyBean{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
