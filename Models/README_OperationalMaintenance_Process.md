Operational Maintenance Process — ProBuild & FixPro

Author: Joel Shelvi
Sprint: AS-IS Strategic BPMN / Operational BPMN Implementation

Purpose

This BPMN model represents the AS-IS external maintenance process between ProBuild Supplies Ltd and FixPro Ltd. It is designed to map the real-world weekly collaboration cycle for tool servicing, compliance testing and return-to-hire readiness.

The objective is to transform the written case study requirements into an executable operational workflow that later supports Camunda automation and integration with API-driven tool management systems.

Scope
Included

- Tagging, logging and preparing tools for maintenance
- Weekly collection and return cycle
- Digital checklist and compliance reporting
- FixPro servicing, testing and labelling
- Updating portals and resyncing internal systems

Not Included

- Customer booking and hire payment process
- ProBuild invoicing / finance
- Detailed technical repair and mechanical steps

Key Actors (Swimlanes)
Organisation	Role
ProBuild	Warehouse Staff, Warehouse Supervisor, Operations Manager
FixPro	Driver, Technicians, Account Manager, Client Portal System

This aligns directly with the SD and SR models for traceability.

Artefacts (Data Objects Used)

-  Digital Handover Checklist
-  Service Completion Report
-  API Sync Update
-  Compliance and Safety Labels

Max artefacts: within the allowed ≤ 8 constraint

Why the Subprocess Is Used

The FixPro servicing facility is abstracted into a subprocess to keep the model strategic, readable, and aligned to BPMN-2.0 best practice.

KPIs Enabled by the Model
KPI Requirement	Where Reflected
90% serviced within 5 days	workflow cycle time
85% first-time fix	inspection + classification
100% safety compliance	test + label stage
Repairs > £50 authorised	gateway decision
Strategic Value

This model provides the foundation for:

Workflow automation

API-based system integration

SLA measurement and dashboarding

Digital audit trail for compliance
