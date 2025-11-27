Strategic Process Modelling Requirements (AS-IS)
External Maintenance Process - ProBuild & FixPro

Written by Joel Shelvi

1. Overview

This document outlines the requirements for the Strategic Process Model (BPMN) for the External Maintenance Process between ProBuild Supplies Ltd and FixPro Ltd.
The aim is to capture the high-level, AS-IS process that both organisations follow when ProBuild sends tools out for servicing and receives them back.
This model will later support the operational BPMN and future Camunda automation work.

2. Scope
Included

How ProBuild identifies tools that need maintenance

Weekly collection and return cycle by FixPro

Servicing, testing and reporting done by FixPro

Returning serviced tools to ProBuild and preparing them for hire

Out of Scope

Customer-facing hire process (booking, payment)

ProBuild internal finance / invoicing

Technical repair steps in full detail

3. Actors (Swimlanes)

The BPMN must include the following roles from the case study:

ProBuild Warehouse Staff

ProBuild Warehouse Supervisor

ProBuild Operations Manager / Tool Hire Operations

FixPro Driver

FixPro Technicians (including testing & labelling)

FixPro Account Manager

FixPro Client Portal / Online System

These represent the main people/systems involved in the maintenance cycle.

4. High-Level Process Steps (Max 10 Tasks)

The BPMN should stay at strategic level, covering the full maintenance loop:

ProBuild tags and logs tools that need maintenance

ProBuild prepares tools for weekly FixPro collection

Warehouse Supervisor + FixPro Driver complete the digital handover checklist

FixPro Driver collects tools and transports them to the service facility

Technicians carry out initial inspection and diagnostics

Technicians classify the tool (routine service / repair / decommission)

Service work + testing + labelling

FixPro updates the portal with service reports and compliance info

FixPro returns serviced tools to ProBuild

ProBuild rescans tools and moves them to the ready-to-hire area

If needed, steps 5–7 can be grouped into a subprocess.

*Note: BPMN does not natively represent hard/soft goals (as in i* models). These goals are included here as strategic requirements that the BPMN process must support.*

5. Hard Goals (KPIs)

These KPIs must be shown in the model as constraints or decision points:

90% of tools serviced within 5 business days

85% minimum first-time fix rate

100% compliance with UK safety standards

Repairs over £50 must be authorised by ProBuild

6. Soft Goals (Shown as Notes/Annotations)

Safety of tools and customers

Fast and efficient turnaround

Minimising downtime

Keeping the hire fleet ready

Allowing ProBuild to avoid having its own workshop

Better fleet optimisation through service data

7. Artefacts / Data Objects (Max 8)

The BPMN should use up to 8 artefacts total:

Tool Tag / Scan Log

Digital Handover Checklist

Service Summary Report

Exceptions Report

Compliance Test Results / Labels

Cost Breakdown / Repair Details

API Data Exchange

Fleet Review / KPI Dashboard

8. Modelling Rules

Use BPMN 2.0 (Camunda compatible)

Only one strategic model

Use message flows between ProBuild ↔ FixPro

Use exclusive gateways for decisions like:

First-time fix

Compliance pass/fail

Repairs costing over £50

The model should be simple and readable

It must link logically to the SD/SR models completed earlier

9. Relationship to SD and SR Models

All actors, goals and dependencies in this BPMN should match the SD and SR models developed in Sprint 1 and refined in Sprint 2. This ensures full traceability across the modelling stages.
