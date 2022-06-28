package edu.esprit.enums;

public enum ReqStatus {
    unseen,processing,denied,complete;
    public static ReqStatus getStatus(String str){
        return switch (str.toLowerCase()) {
            case "processing" -> processing;
            case "denied" -> denied;
            case "complete" -> complete;
            default -> unseen;
        };
    }
}
