package com.example.qrteach;

public class HelpSheets {
    String id, worksheetName, worksheetSummary, worksheetInstructions, links, teacherID;

    public HelpSheets() {

    }

    public HelpSheets(String id, String worksheetName, String worksheetSummary, String worksheetInstructions, String links, String teacherID) {
        this.id = id;
        this.worksheetName = worksheetName;
        this.worksheetSummary = worksheetSummary;
        this.worksheetInstructions = worksheetInstructions;
        this.links = links;
        this.teacherID = teacherID;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getWorksheetName() {
        return worksheetName;
    }

    public void setWorksheetName(String worksheetName) {
        this.worksheetName = worksheetName;
    }

    public String getWorksheetSummary() {
        return worksheetSummary;
    }

    public void setWorksheetSummary(String worksheetSummary) {
        this.worksheetSummary = worksheetSummary;
    }

    public String getWorksheetInstructions() {
        return worksheetInstructions;
    }

    public void setWorksheetInstructions(String worksheetInstructions) {
        this.worksheetInstructions = worksheetInstructions;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }
}
