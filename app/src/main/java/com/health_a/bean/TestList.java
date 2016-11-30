package com.health_a.bean;

/**
 * Created by Admin on 2016/8/2. 测试数据
 */
public class TestList {

    public static class DataEntity{
        private String testName;
        private String testResult;
        private String testTime;
        private String testDoctor;

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getTestResult() {
            return testResult;
        }

        public void setTestResult(String testResult) {
            this.testResult = testResult;
        }

        public String getTestTime() {
            return testTime;
        }

        public void setTestTime(String testTime) {
            this.testTime = testTime;
        }

        public String getTestDoctor() {
            return testDoctor;
        }

        public void setTestDoctor(String testDoctor) {
            this.testDoctor = testDoctor;
        }

        @Override
        public String toString() {
            return "DataEntily{" +
                    "testName='" + testName + '\'' +
                    ", testResult='" + testResult + '\'' +
                    ", testTime='" + testTime + '\'' +
                    ", testDoctor='" + testDoctor + '\'' +
                    '}';
        }

    }
}
