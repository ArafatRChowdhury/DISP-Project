## Operational Maintenance Process — ProBuild & FixPro

**Author:** Joel Shelvi  
**Sprint:** AS-IS Strategic BPMN / Operational BPMN Implementation  
**Role:** BPMN Lead / Project Manager 



## Alignment & Traceability  
This operational BPMN aligns with:  
- **SR Model v1.0 — External Maintenance Requirements**  
- **SD Model v2.x — Dependencies and Goal/Resource mapping between ProBuild & FixPro**  

This ensures end-to-end traceability from **requirements → strategic design → operational workflow → Camunda implementation**.

## Modelling Approach Documentation

Detailed BPMN modelling methodology, task-type justification, and design rationale are documented in:

- BPMN_Modelling_Approach_and_Thought_Process.docx


## Purpose  
This BPMN model represents the **AS-IS external maintenance cycle** between ProBuild Supplies Ltd and FixPro Ltd.  
It maps the real-world weekly collaboration flow for **tool servicing, compliance testing, reporting and readiness for re-hire**.

The objective is to **convert written case study requirements into an executable operational workflow** that prepares the foundation for:
- Future Camunda automation
- Integration with API-driven tool management systems
- Digital service reporting and audit compliance



### Scope

#### **Included**
- Tagging, logging and preparing tools for maintenance  
- Weekly collection and return cycle  
- Digital checklist, compliance handover and reporting  
- FixPro inspection, servicing, testing & labelling  
- Updating portals / resyncing internal systems  
- Return readiness for hire  

#### **Not Included**
- Customer booking & hire payment process  
- Full repair specification or engineering detail  
- Finance/credit operations with FinTrust  
- Business intelligence & long-term fleet optimisation (TO-BE phase)



### Key Actors / Swimlanes
ProBuild Supplies Ltd

FixPro Ltd



## Data Objects Used
| Data Artifact | Purpose |
| Tool Tag / Scan Log | Identify tools requiring maintenance |
| Digital Handover Checklist | Verify chain of custody |
| Service & Completion Report | Transparency and record |
| Repair/Cost Breakdown | Authorisation control |
| Compliance Test Labels | Safety validation |
| API Sync Data | System-to-system updating |
| Ready-to-Hire Confirmation | Return to inventory |



### Next Steps (TO-BE Workflow)
- Map automation candidates for user tasks  
- Define external form requirements (Camunda Forms)  
- Apply API worker & web service integration patterns  

---

