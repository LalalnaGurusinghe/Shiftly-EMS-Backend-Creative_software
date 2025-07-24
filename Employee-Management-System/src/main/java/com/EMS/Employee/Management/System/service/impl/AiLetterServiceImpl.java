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
        // Define required fields for each letter type
        Map<String, String[]> requiredFieldsMap = new HashMap<>();
        requiredFieldsMap.put("EPF_ETF_NAME_CHANGE_LETTER", new String[]{"employeeId","currentName","newName","nicNumber","epfNumber","etfNumber","reasonForChange","effectiveDate","supervisorName","signingPersonPosition"});
        requiredFieldsMap.put("SKILL_ASSESSMENT_LETTER", new String[]{"employeeId","fullName","position","department","skillsToAssess","assessmentPurpose","supervisorName","signingPersonPosition"});
        requiredFieldsMap.put("SALARY_UNDERTAKING_LETTER", new String[]{"employeeId","fullName","position","currentSalary","salaryFrequency","undertakingPurpose","authorizedPersonName","signingPersonPosition"});
        requiredFieldsMap.put("SALARY_CONFIRMATION_LETTER", new String[]{"employeeId","fullName","position","department","currentSalary","currency","confirmationPurpose","authorizedPersonName","signingPersonPosition"});
        requiredFieldsMap.put("EMPLOYMENT_CONFIRMATION_LETTER", new String[]{"employeeId","fullName","position","department","employmentType","workingHours","reportingManager","confirmationPurpose","signingPersonPosition"});
        // Default: only recipientName and additionalDetails required
        requiredFieldsMap.put("DEFAULT", new String[]{"recipientName","additionalDetails"});

        // Filter out empty non-required fields
        String[] requiredFields = requiredFieldsMap.getOrDefault(letterType, requiredFieldsMap.get("DEFAULT"));
        Map<String, Object> filteredFields = new HashMap<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            boolean isRequired = false;
            for (String req : requiredFields) {
                if (req.equals(entry.getKey())) {
                    isRequired = true;
                    break;
                }
            }
            if (isRequired || (entry.getValue() != null && !entry.getValue().toString().trim().isEmpty())) {
                filteredFields.put(entry.getKey(), entry.getValue());
            }
        }
        String prompt = buildPrompt(letterType, filteredFields);
        if (prompt == null) return null;
        // Prepend explicit instruction for Gemini
        String instruction = "Write a formal ";
        switch (letterType) {
            case "EPF_ETF_NAME_CHANGE_LETTER":
                instruction += "EPF/ETF name change letter from a manager/authorized person about the employee, using the following details:\n\n";
                break;
            case "SKILL_ASSESSMENT_LETTER":
                instruction += "skill assessment request letter from a manager/authorized person about the employee, using the following details:\n\n";
                break;
            case "SALARY_UNDERTAKING_LETTER":
                instruction += "salary undertaking letter from a manager/authorized person about the employee, using the following details:\n\n";
                break;
            case "SALARY_CONFIRMATION_LETTER":
                instruction += "salary confirmation letter from a manager/authorized person about the employee, using the following details:\n\n";
                break;
            case "EMPLOYMENT_CONFIRMATION_LETTER":
                instruction += "employment confirmation letter from a manager/authorized person about the employee, using the following details:\n\n";
                break;
            default:
                instruction += "letter using the following details:\n\n";
        }
        prompt = instruction + prompt;
        return geminiClient.generateLetter(prompt);
    }

    private String buildPrompt(String letterType, Map<String, Object> fields) {
        // Updated templates to match new frontend field names and additional fields
        Map<String, String> templates = new HashMap<>();
        templates.put("EPF_ETF_NAME_CHANGE_LETTER", "Subject: Request for Name Change in EPF/ETF Records\n\nDear Sir/Madam,\n\nThis letter is to formally request, on behalf of {currentName} (Employee ID: {employeeId}, NIC: {nicNumber}, EPF: {epfNumber}, ETF: {etfNumber}), a change of name in the EPF/ETF records.\n\nCurrent name: {currentName}\nNew name: {newName}\nReason for name change: {reasonForChange}\nEffective date: {effectiveDate}\n\nPlease update your records accordingly.\n\nThank you.\n\nSincerely,\n{supervisorName}\nPosition: {signingPersonPosition}");
        templates.put("SKILL_ASSESSMENT_LETTER", "Subject: Request for Skill Assessment\n\nTo Whom It May Concern,\n\nThis is to request, on behalf of {fullName} (Employee ID: {employeeId}), currently holding the position of {position} in the {department} department, a skill assessment for the following skills:\n\nSkills to be assessed: {skillsToAssess}\nPurpose of assessment: {assessmentPurpose}\n\nWe appreciate your consideration of this request.\n\nThank you.\n\nSincerely,\n{supervisorName}\nPosition: {signingPersonPosition}\nJoin Date: {joinDate}\nRequesting Organization: {requestingOrganization}");
        templates.put("SALARY_UNDERTAKING_LETTER", "Subject: Salary Undertaking\n\nTo Whom It May Concern,\n\nThis is to certify, on behalf of {fullName} (Employee ID: {employeeId}), holding the position of {position}, that the current salary is {currentSalary} {currency} ({salaryFrequency}).\n\nPurpose of undertaking: {undertakingPurpose}\n\nThis letter is issued upon the request of {fullName} for submission to {requestingParty}.\n\nIf further information is required, please feel free to contact us.\n\nSincerely,\n{authorizedPersonName}\nPosition: {signingPersonPosition}");
        templates.put("SALARY_CONFIRMATION_LETTER", "Subject: Salary Confirmation\n\nTo Whom It May Concern,\n\nThis is to confirm, on behalf of {fullName} (Employee ID: {employeeId}), employed as {position} in the {department} department since {joinDate}, that the current salary is {currentSalary} {currency}.\nAllowances: {allowances}\n\nPurpose of confirmation: {confirmationPurpose}\n\nThis letter is issued upon the request of the employee.\n\nSincerely,\n{authorizedPersonName}\nPosition: {signingPersonPosition}");
        templates.put("EMPLOYMENT_CONFIRMATION_LETTER", "Subject: Employment Confirmation\n\nTo Whom It May Concern,\n\nThis is to confirm, on behalf of {fullName} (Employee ID: {employeeId}), employed as {position} in the {department} department since {joinDate}.\n\nEmployment type: {employmentType}\nWorking hours: {workingHours}\nReporting manager: {reportingManager}\n\nPurpose of confirmation: {confirmationPurpose}\n\nThis letter is issued upon the request of the employee.\n\nSincerely,\n{authorizedPersonName}\nPosition: {signingPersonPosition}");
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