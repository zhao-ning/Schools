package com.example.myshools.Consult.Entity;

import com.example.myshools.Mine.student.StudentReport;
import com.example.myshools.entity.Activities;

import java.util.List;

public class GetReportResultInfo {

    private List<StudentReport> records;
    private long total;
    private long size;
    private long current;
    private boolean optimizeCountSql;
    private boolean isSearchCount;
    private boolean hitCount;
    private String countId;
    private Long maxLimit;



    public List<StudentReport> getRecords() {
        return records;
    }

    public void setRecords(List<StudentReport> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public boolean isOptimizeCountSql() {
        return optimizeCountSql;
    }

    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    public boolean isSearchCount() {
        return isSearchCount;
    }

    public void setSearchCount(boolean searchCount) {
        isSearchCount = searchCount;
    }

    public boolean isHitCount() {
        return hitCount;
    }

    public void setHitCount(boolean hitCount) {
        this.hitCount = hitCount;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    public Long getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public String toString() {
        return "GetActivityResultInfo{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", optimizeCountSql=" + optimizeCountSql +
                ", isSearchCount=" + isSearchCount +
                ", hitCount=" + hitCount +
                ", countId='" + countId + '\'' +
                ", maxLimit=" + maxLimit +
                '}';
    }
}
