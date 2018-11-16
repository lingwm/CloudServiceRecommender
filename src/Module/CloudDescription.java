package Module;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class CloudDescription implements CaseComponent{
    Integer price;
    String region;
    Double CPUPower;
    Integer CPUCores;
    Integer memory;
    Integer storage;
    Integer bandwidth;
    Integer IOPerformance;
    Integer AvgResponseTime;
    Integer MaxLatency;
    Integer MaxUsers;
    Integer availability;
    Integer tiers;
    String id;

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("id", this.getClass());
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getCPUPower() {
        return CPUPower;
    }

    public void setCPUPower(Double CPUPower) {
        this.CPUPower = CPUPower;
    }

    public Integer getCPUCores() {
        return CPUCores;
    }

    public void setCPUCores(Integer CPUCores) {
        this.CPUCores = CPUCores;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Integer getIOPerformance() {
        return IOPerformance;
    }

    public void setIOPerformance(Integer IOPerformance) {
        this.IOPerformance = IOPerformance;
    }

    public Integer getAvgResponseTime() {
        return AvgResponseTime;
    }

    public void setAvgResponseTime(Integer avgResponseTime) {
        AvgResponseTime = avgResponseTime;
    }

    public Integer getMaxLatency() {
        return MaxLatency;
    }

    public void setMaxLatency(Integer maxLatency) {
        MaxLatency = maxLatency;
    }

    public Integer getMaxUsers() {
        return MaxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        MaxUsers = maxUsers;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Integer getTiers() {
        return tiers;
    }

    public void setTiers(Integer tiers) {
        this.tiers = tiers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}