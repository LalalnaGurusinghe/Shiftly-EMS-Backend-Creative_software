package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.service.AiLetterService;
import com.EMS.Employee.Management.System.service.GeminiClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiLetterServiceImpl implements AiLetterService {
    private final GeminiClient geminiClient;

    public AiLetterServiceImpl(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    @Override
    public String generateLetter(String letterType, Map<String, Object> fields) {
        String prompt = buildPrompt(letterType, fields);
        if (prompt == null) return null;
        // Prepend explicit instruction for Gemini
        String instruction = "Write a formal ";
        switch (letterType) {
            case "EPF_ETF_NAME_CHANGE_LETTER":
                instruction += "EPF/ETF name change letter using the following details:\n\n";
                break;
            case "SKILL_ASSESSMENT_LETTER":
                instruction += "skill assessment request letter using the following details:\n\n";
                break;
            case "SALARY_UNDERTAKING_LETTER":
                instruction += "salary undertaking letter using the following details:\n\n";
                break;
            case "SALARY_CONFIRMATION_LETTER":
                instruction += "salary confirmation letter using the following details:\n\n";
                break;
            case "EMPLOYMENT_CONFIRMATION_LETTER":
                instruction += "employment confirmation letter using the following details:\n\n";
                break;
            default:
                instruction += "letter using the following details:\n\n";
        }
        prompt = instruction + prompt;
        return geminiClient.generateLetter(prompt);
    }

    private String buildPrompt(String letterType, Map<String, Object> fields) {
        // Use templates from previous discussion
        Map<String, String> templates = new HashMap<>();
        templates.put("EPF_ETF_NAME_CHANGE_LETTER", "Subject: Request for Name Change in EPF/ETF Records\n\nDear Sir/Madam,\n\nI, {currentName}, holding Employee ID {employeeId}, NIC Number {nicNumber}, EPF Number {epfNumber}, and ETF Number {etfNumber}, kindly request a change of my name in the EPF/ETF records.\n\nMy current name is: {currentName}\nMy new name is: {newName}\nReason for name change: {reasonForChange}\nEffective date of name change: {effectiveDate}\n\nI kindly request you to update your records accordingly.\n\nThank you.\n\nSincerely,\n{currentName}");
        templates.put("SKILL_ASSESSMENT_LETTER", "Subject: Request for Skill Assessment\n\nTo Whom It May Concern,\n\nI am {fullName}, currently employed as {position} in the {department} department (Employee ID: {employeeId}). I am writing to request a skill assessment for the following skills:\n\nSkills to be assessed: {skillsToAssess}\nPurpose of assessment: {assessmentPurpose}\n\nI would appreciate your consideration of this request.\n\nThank you.\n\nSincerely,\n{fullName}");
        templates.put("SALARY_UNDERTAKING_LETTER", "Subject: Salary Undertaking\n\nTo Whom It May Concern,\n\nThis is to certify that {fullName} (Employee ID: {employeeId}), holding the position of {position}, is currently employed with us.\n\nCurrent salary: {currentSalary} ({salaryFrequency})\nPurpose of undertaking: {undertakingPurpose}\n\nThis letter is issued upon the request of {fullName} for submission to {requestingParty}.\n\nIf you require further information, please feel free to contact us.\n\nSincerely,\n[Authorized Signatory]");
        templates.put("SALARY_CONFIRMATION_LETTER", "Subject: Salary Confirmation\n\nTo Whom It May Concern,\n\nThis is to confirm that {fullName} (Employee ID: {employeeId}) is employed as {position} in the {department} department since {joinDate}.\n\nCurrent salary: {currentSalary} {currency}\nAllowances: {allowances}\n\nPurpose of confirmation: {confirmationPurpose}\n\nThis letter is issued upon the request of the employee.\n\nSincerely,\n[Authorized Signatory]");
        templates.put("EMPLOYMENT_CONFIRMATION_LETTER", "Subject: Employment Confirmation\n\nTo Whom It May Concern,\n\nThis is to confirm that {fullName} (Employee ID: {employeeId}) is employed as {position} in the {department} department since {joinDate}.\n\nEmployment type: {employmentType}\nWorking hours: {workingHours}\nReporting manager: {reportingManager}\n\nPurpose of confirmation: {confirmationPurpose}\n\nThis letter is issued upon the request of the employee.\n\nSincerely,\n[Authorized Signatory]");
        templates.put("DEFAULT", "Subject: General Request\n\nDear {recipientName},\n\n{additionalDetails}\n\nThank you.\n\nSincerely,\n[Your Name]");

        String template = templates.getOrDefault(letterType, templates.get("DEFAULT"));
        if (template == null) return null;
        // Replace placeholders with field values
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue() == null ? "" : entry.getValue().toString());
        }
        return template;
    }
} 