package shcForm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SHCFormsGenerator {

    static String DIRECTORYPATH = "/Users/meghan/Desktop/SHC Docs/";
    static String LATEXPATH = "/usr/texbin/pdflatex";

    private static String initialsToName(String i) throws FileNotFoundException {
	//Members.txt is a text file of all the members of the committee
	//1 member per line, in the format "First Last, FL"
	File committeeMemberFullNames = new File(DIRECTORYPATH+"members.txt");
	Scanner shcMembers= new Scanner(committeeMemberFullNames);
	while(shcMembers.hasNextLine()) {
	    String curLine = shcMembers.nextLine();
	    if(curLine.contains(i)) {
		shcMembers.close();
		return curLine.split(",")[0];
	    }
	}
	shcMembers.close();
	return i;
    }

    public static void main(String[] args) throws IOException {

	
	File caseInformation = new File("input.txt");
	Scanner inputFHC= new Scanner(caseInformation);
	Scanner inputCS = new Scanner(caseInformation);
	Scanner inputDEAN = new Scanner(caseInformation);

	String caseNumber=inputDEAN.nextLine();
	
	//Generate output files
	StringBuilder outputFileNames = new StringBuilder().append(caseNumber);
	outputFileNames.setCharAt(5,'_');
	BufferedWriter fhcletter= new BufferedWriter(new FileWriter(outputFileNames+"_FHC.tex"));
	BufferedWriter deanletter= new BufferedWriter(new FileWriter(outputFileNames+"_DEAN.tex"));
	BufferedWriter casesummary= new BufferedWriter(new FileWriter(outputFileNames+"_CS.tex"));

	//Header
	deanletter.write("\\documentclass[11pt]{article}\n\\usepackage[margin=1.25in]{geometry}\n\\setlength\\parindent{0pt}\n\\newcommand{\\coordinator}{Meghan McCreary}");
	fhcletter.write("\\documentclass[11pt]{article}\n\\usepackage[margin=1.25in]{geometry}\n\\setlength\\parindent{0pt}\n\\newcommand{\\coordinator}{Meghan McCreary}");
	casesummary.write("\\documentclass[11pt]{article}\n\\usepackage[margin=1.25in]{geometry}\n\\setlength\\parindent{0pt}\n\\newcommand{\\coordinator}{Meghan McCreary}");
	
	//Ignore first four lines as they aren't needed for case summary/fhc letter
	for(int i=0;i<4;i++) {
	    inputCS.nextLine();
	    inputFHC.nextLine();
	}
	
	deanletter.write("\n\\newcommand{\\caseNumber}{"+caseNumber+"}");
	fhcletter.write("\n\\newcommand{\\caseNumber}{"+caseNumber+"}");
	casesummary.write("\n\\newcommand{\\caseNumber}{"+caseNumber+"}");
	
	//Store case number and division
	//Write professor name and student name to file
	deanletter.write("\n\n\\newcommand{\\stuname}{"+inputDEAN.nextLine()+", }\n\\newcommand{\\profname}{, taught by "+inputDEAN.nextLine()+"}");
	String division = inputDEAN.nextLine();

	//Correctly address letter depending on student's division
	if(division.contains("con")) {
	    deanletter.write("\n\\newcommand{\\greeting}{Dear Dean Kalyn,}");
	    deanletter.write("\n\\newcommand{\\concollege}{conservatory}");
	    fhcletter.write("\n\\newcommand{\\concollege}{conservatory}");
	    casesummary.write("\n\\newcommand{\\concollege}{conservatory}");
	}
	else if(division.contains("d")) {
	    deanletter.write("\n\\newcommand{\\greeting}{Dear Dean Kalyn and Dean Elgren,}");
	    deanletter.write("\n\\newcommand{\\concollege}{double degree}");
	    fhcletter.write("\n\\newcommand{\\concollege}{double degree}");
	    casesummary.write("\n\\newcommand{\\concollege}{double degree}");
	}
	else {
	    deanletter.write("\n\\newcommand{\\greeting}{Dear Dean Elgren,}");
	    deanletter.write("\n\\newcommand{\\concollege}{college}");
	    fhcletter.write("\n\\newcommand{\\concollege}{college}");
	    casesummary.write("\n\\newcommand{\\concollege}{college}");
	}

	//FHC anon
	fhcletter.write("\n\n\\newcommand{\\stuname}{}\n\\newcommand{\\profname}{}\n\\newcommand{\\greeting}{Dear Faculty Honor Committee,}\n\n\n");

	//Case number, date of hearing, violation - DEAN/FHC
	fhcletter.write("\n\\newcommand{\\dateofhearing}{"+inputFHC.nextLine()+"}");
	fhcletter.write("\n\\newcommand{\\violation}{"+inputFHC.nextLine()+"}");
	deanletter.write("\n\\newcommand{\\dateofhearing}{"+inputDEAN.nextLine()+"}");
	deanletter.write("\n\\newcommand{\\violation}{"+inputDEAN.nextLine()+"}");
	casesummary.write("\n\\newcommand{\\dateofhearing}{"+inputCS.nextLine()+"}");
	casesummary.write("\n\\newcommand{\\violation}{"+inputCS.nextLine()+"}");

	//Class year
	String classyear = inputDEAN.nextLine();
	inputFHC.nextLine();
	inputCS.nextLine();
	
	casesummary.write("\n\\newcommand{\\classyear}{"+classyear+"}");
	casesummary.write("\n\\newcommand{\\violyear}{"+inputCS.nextLine()+"}");
	String deptCourse = inputCS.nextLine();
	
	switch(classyear) {
    	    case "1": classyear = classyear.concat("st");
    	        break;
    	    case "2": classyear = classyear.concat("nd");
	        break;
	    case "3": classyear = classyear.concat("rd");
    	        break;
    	    default: classyear = classyear.concat("th");
    	    	break;
	}
	
	fhcletter.write("\n\\newcommand{\\classyear}{"+classyear+"}");
	deanletter.write("\n\\newcommand{\\classyear}{"+classyear+"}");
	
	//Ignore violation year, votes for the letters
	for(int i=0;i<5;i++) {
	    inputDEAN.nextLine();
	    inputFHC.nextLine();
	}
	
	fhcletter.write("\n\\newcommand{\\deptCourse}{"+deptCourse+"}");
	deanletter.write("\n\\newcommand{\\deptCourse}{"+deptCourse+"}");
	casesummary.write("\n\\newcommand{\\deptCourse}{"+deptCourse+"}");

	//Votes
	String resp=inputCS.nextLine();
	String notResp=inputCS.nextLine();
	String abstains=inputCS.nextLine();
	boolean responsible=true;


	//Determine responsibility 
	casesummary.write("\n\\newcommand{\\responsible}{"+resp+"}");
	casesummary.write("\n\\newcommand{\\notResp}{"+notResp+"}");
	casesummary.write("\n\\newcommand{\\abstain}{"+abstains+"}");

	if(resp.contains("4") || resp.contains("5") ) {
	    if(notResp.contains("0")) {
		fhcletter.write("\n\\newcommand{\\vote}{unanimously}");
		deanletter.write("\n\\newcommand{\\vote}{unanimously}");
	    }
	    else {
		fhcletter.write("\n\\newcommand{\\vote}{by a 4 to 1 vote}");
		deanletter.write("\n\\newcommand{\\vote}{by a 4 to 1 vote}");
	    }
	}
	else {
	    responsible=false;

	    if(resp.contains("0")) {
		fhcletter.write("\n\\newcommand{\\vote}{unanimously}");
		deanletter.write("\n\\newcommand{\\vote}{unanimously}");
	    }
	    else {
		fhcletter.write("\n\\newcommand{\\vote}{by a "+notResp+" to "+resp+" vote}");
		deanletter.write("\n\\newcommand{\\vote}{by a "+notResp+" to "+resp+" vote}");
	    }
	}

	//evidence
	fhcletter.write("\n\\newcommand{\\evidence}{"+inputFHC.nextLine()+"}\n");
	deanletter.write("\n\\newcommand{\\evidence}{"+inputDEAN.nextLine()+"}\n");
	casesummary.write("\n\\newcommand{\\evidence}{"+inputCS.nextLine()+"}\n");

	String intOrExt = inputCS.nextLine();
	inputDEAN.nextLine();
	inputFHC.nextLine();

	inputCS.nextLine();
	fhcletter.write("\n\\newcommand{\\duedate}{"+inputFHC.nextLine()+"}");
	deanletter.write("\n\\newcommand{\\duedate}{"+inputDEAN.nextLine()+"}");	
	fhcletter.write("\n\\newcommand{\\sanction}{"+inputFHC.nextLine()+"}");
	casesummary.write("\n\\newcommand{\\sanction}{"+inputCS.nextLine()+"}");
	deanletter.write("\n\\newcommand{\\sanction}{"+inputDEAN.nextLine()+"}");

	inputFHC.close();
	inputDEAN.close();
	
	if(!responsible) {
	    fhcletter.write("\n\\newcommand{\\intorext}{}\n");
	    deanletter.write("\n\\newcommand{\\intorext}{}\n");
	    casesummary.write("\n\\newcommand{\\intorext}{n/a}");
	    fhcletter.write("\n\\newcommand{\\respOrNo}{ not}");
	    deanletter.write("\n\\newcommand{\\respOrNo}{ not}");
	    Scanner fhcFile = new Scanner(new File("FHC.txt"));
	    boolean done=true;
	    while(fhcFile.hasNext()&&done) {
		String temp=fhcFile.nextLine();
		fhcletter.write(temp+"\n");
		deanletter.write(temp+"\n");
		if(temp.contains("The evidence")) {
		    fhcFile.nextLine();
		    fhcFile.nextLine();
		    fhcFile.nextLine();
		    fhcFile.nextLine();
		}
	    }
	    fhcFile.close();
	}

	else {
	    fhcletter.write("\n\\newcommand{\\respOrNo}{}");
	    deanletter.write("\n\\newcommand{\\respOrNo}{}");
	    if(intOrExt.contains("ext")) {
		fhcletter.write("\n\\newcommand{\\intorext}{As specified in the Honor Code, this disciplinary action will be noted on the respondent's permanent record, with an indication that the reason for the notation is a violation of the Honor Code.}\n");
		casesummary.write("\n\\newcommand{\\intorext}{external}");
		Path FHC_file = Paths.get(DIRECTORYPATH+"FHC_2nd.txt");
		byte[] fileArray;
		fileArray = Files.readAllBytes(FHC_file);
		for(byte mybyte:fileArray) {
		    fhcletter.write(mybyte);
		}
	    }
	    else {
		fhcletter.write("\n\\newcommand{\\intorext}{This decision is to be marked for internal use only.}\n\n");
		deanletter.write("\n\\newcommand{\\intorext}{This decision is to be marked for internal use only.}\n\n");
		casesummary.write("\n\\newcommand{\\intorext}{internal}");
		Path FHC_file = Paths.get(DIRECTORYPATH+"FHC.txt");
		byte[] fileArray;
		fileArray = Files.readAllBytes(FHC_file);
		for(byte mybyte:fileArray) {
		    fhcletter.write(mybyte);
		    deanletter.write(mybyte);
		}
	    }
	}
	fhcletter.close();
	deanletter.close();

	//FHC to pdf
	String strFHC=null;
	try {
	    Process p = Runtime.getRuntime().exec(LATEXPATH+" "+outputFileNames+"_FHC");
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    while ((strFHC = stdInput.readLine()) != null) {}
	    while ((strFHC = stdError.readLine()) != null) {}
	} catch (Exception e) {
	    System.out.println("exception happened - here's what I know: ");
	    e.printStackTrace();
	    System.exit(-1);
	}

	//Dean to pdf
	String strDEAN=null;
	try {
	    Process pDEAN = Runtime.getRuntime().exec(LATEXPATH+" "+outputFileNames+"_DEAN");
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(pDEAN.getInputStream()));
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(pDEAN.getErrorStream()));
	    while ((strDEAN = stdInput.readLine()) != null) {}
	    while ((strDEAN = stdError.readLine()) != null) {}
	} catch (Exception e) {
	    System.out.println("exception happened - here's what I know: ");
	    e.printStackTrace();
	    System.exit(-1);
	}

	//Finish case summary information
	casesummary.write("\n\\newcommand{\\reasonForSanc}{"+inputCS.nextLine()+"}");
	
	if(Character.isLetter(caseNumber.charAt(caseNumber.length()-1))){
	    casesummary.write("\n\\newcommand{\\others}{yes}");
	}
	else {
	    casesummary.write("\n\\newcommand{\\others}{no}");
	};
	
	casesummary.write("\n\\newcommand{\\major}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\atHearing}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\firstClass}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\selfReport}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\admit}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\passClass}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\stuStatus}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\prefAddress}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\followup}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\caseSum}{"+inputCS.nextLine()+"}"
		+ "\n\\newcommand{\\mitigating}{"+inputCS.nextLine()+"}" );

	casesummary.write("\n\\newcommand{\\casemanagers}{");
	String[] caseManagers = inputCS.nextLine().replaceAll("\\s","").split(",");
	for(int i=0; i<caseManagers.length; i++) {
	    casesummary.write(initialsToName(caseManagers[i]));
	    if(i!=caseManagers.length-1) {
		casesummary.write(", ");
	    }
	}
	casesummary.write("}\n\\newcommand{\\HearingPanelists}{");
	String[] hearingPanelists=inputCS.nextLine().replaceAll("\\s","").split(",");
	inputCS.close();
	for(int i=0;i<hearingPanelists.length;i++) {
	    casesummary.write(initialsToName(hearingPanelists[i]));
	    if(i!=hearingPanelists.length-1) {
		casesummary.write(", ");
	    }
	}
	casesummary.write("}\n\n");

	//Writes template to file
	Path CS_file = Paths.get(DIRECTORYPATH+"CS.txt");
	byte[] fileArray2;
	fileArray2 = Files.readAllBytes(CS_file);

	for(byte mybyte:fileArray2) {
	    casesummary.write(mybyte);
	}

	casesummary.close();
	
	//Case summary to pdf
	String strCS=null;
	try {
	    Process p1 = Runtime.getRuntime().exec(LATEXPATH+" "+outputFileNames+"_CS");
	    BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
	    BufferedReader stdError1 = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
	    while ((strCS = stdInput1.readLine()) != null) {}
	    while ((strCS = stdError1.readLine()) != null) {}
	} catch (Exception e) {
	    System.out.println("exception happened - here's what I know: ");
	    e.printStackTrace();
	    System.exit(-1);
	}

	//Deletes .aux and .log files
	File folder = new File(".");
	File[] fList = folder.listFiles();
	for (int i = 0; i < fList.length; i++) {
	    String pes = fList[i].getName();
	    if (pes.endsWith(".aux")||pes.endsWith(".log")) {
		boolean success = new File(fList[i].getName()).delete();
	    }
	}

    }
}
