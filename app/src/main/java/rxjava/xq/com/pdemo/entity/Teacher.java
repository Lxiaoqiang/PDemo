package rxjava.xq.com.pdemo.entity;

import java.util.ArrayList;

public class Teacher extends BaseEntity{

    /**
     * code : 200
     * message : 25
     * data : [{"userID":"17930468","price":"45","dist":"0.4835897666146","sortScore":"0.80865189537525"},{"userID":"12910648","price":"45","dist":"3.445115084264","sortScore":"0.72347399257462"},{"userID":"11120351","price":"45","dist":"5.5212968529467","sortScore":"0.64962327154405"},{"userID":"11650771","price":"45","dist":"0.47775411208294","sortScore":"0.40257027547265"},{"userID":"1430028","price":"45","dist":"0.480448929552","sortScore":"0.40255949620278"},{"userID":"17280784","price":"45","dist":"1.4378434383195","sortScore":"0.39704874528869"},{"userID":"12910690","price":"45","dist":"5.5212968529467","sortScore":"0.37903325787215"},{"userID":"16730323","price":"45","dist":"11.90918741598","sortScore":"0.35348269697805"},{"userID":"17620317","price":"45","dist":"12.097447676208","sortScore":"0.3527286545791"},{"userID":"1060000","price":"45","dist":"12.871442995058","sortScore":"0.31023207221049"},{"userID":"15320343","price":"45","dist":"11.907784361602","sortScore":"0.19741079948337"},{"userID":"18210178","price":"45","dist":"5.5212968529467","sortScore":"0.19079996658821"},{"userID":"18120603","price":"45","dist":"3.445115084264","sortScore":"0.18621953966294"},{"userID":"16840749","price":"45","dist":"3.4642918202486","sortScore":"0.18614283271901"},{"userID":"18140643","price":"45","dist":"12.666032133586","sortScore":"0.14933587146566"},{"userID":"1214036","price":"45","dist":"12.871442995058","sortScore":"0.14851422801977"},{"userID":"16920265","price":"45","dist":"13.418168356499","sortScore":"0.147447774574"},{"userID":"16230557","price":"45","dist":"14.301566378557","sortScore":"0.14279373448577"},{"userID":"13700461","price":"45","dist":"678.16732513402","sortScore":"-2.3997846472151"},{"userID":"19520752","price":"45","dist":"1214.8017631119","sortScore":"-4.6592070524477"},{"userID":"17070788","price":"45","dist":"0.016036955119942","sortScore":null},{"userID":"16970787","price":"45","dist":"0.49539520759321","sortScore":null},{"userID":"16140794","price":"45","dist":"1.4378434383195","sortScore":null},{"userID":"16810792","price":"45","dist":"1.4378434383195","sortScore":null},{"userID":"17490792","price":"45","dist":"1.4378434383195","sortScore":null}]
     */

    /**
     * userID : 17930468
     * price : 45
     * dist : 0.4835897666146
     * sortScore : 0.80865189537525
     */
    private String code;
    private int message;
    private ArrayList<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String userID;
        private String price;
        private String dist;
        private String sortScore;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDist() {
            return dist;
        }

        public void setDist(String dist) {
            this.dist = dist;
        }

        public String getSortScore() {
            return sortScore;
        }

        public void setSortScore(String sortScore) {
            this.sortScore = sortScore;
        }
    }
}
