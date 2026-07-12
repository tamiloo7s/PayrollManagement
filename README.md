
# Payroll Management App

A Payroll Management app built in Kotlin for Android. The app lets users create payrolls, add employees to each payroll, and view a summary of wages, taxes, and net pay. It works fully offline, all data are stored locally on the device.


## Features

* View a list of all created payrolls
* Create a new payroll and add one or more employees
* Automatic tax calculation
* View payroll details: per-employee wages/taxes/net, plus a payroll summary
* No internet connection required

## Architecture

This application is built using Clean Architecture together with the MVVM (Model–View–ViewModel) pattern to create a modular, maintainable, and scalable codebase.


## Requirements

* Android Studio: Ladybug (2024.2.1) or newer
* JDK: 17
* Min SDK: 28
* Target SDK: 36
##  How To Run


### Step 1: Install Android Studio
*  Go to https://developer.android.com/studio and click Download Android Studio.
*  Accept the terms and conditions
*  Once setup completes, Android Studio opens to the Welcome screen installation is done.

### Step 2: Create an emulator
*  From the Welcome screen, go to More Actions > Virtual Device Manager (in an open project, this is under Tools > Device Manager).
*  Click Create Device.
*  Pick a device profile (e.g., Pixel 6) and click Next.
*  Select a system image with API 34 (download it if it isn't already available) and click Next.
*  Click Finish. The emulator now appears in your Device Manager list and can be started anytime.

### Step 3: Get the project code
* bash
git clone https://github.com/tamiloo7s/PayrollManagement.git


### Step 4: Open the project
*  From the Android Studio Welcome screen, click Open.
*  Select the cloned payroll-management-android folder and click OK.
*  Wait for Gradle sync to finish

### Step 5: Select a device
* Emulator: choose the virtual device you created in Step 2 from the device dropdown in the toolbar.

### Step 6: Run the app
* Click the green Run ▶ button in the toolbar (or press Shift + F10). Android Studio builds the app and installs it on the selected emulator.

## Future Improvements

* Data export and reporting –> Allow payroll summaries to be exported as CSV for easier record keeping.
* Search and filtering –> Add search, sorting, and filtering for payrolls and employees.
* Dependency Injection – Integrate Hilt to simplify dependency management and improve testability.

## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/ee788e94-93c6-49ac-8d15-6f10e97b712a" width="220">
  <img src="https://github.com/user-attachments/assets/dbfc74da-579e-4dbd-ac45-732de9a71032" width="220">
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/9bf9960c-8475-42a2-8006-0a75bcdde920" width="220">
  <img src="https://github.com/user-attachments/assets/421b7cfe-b15f-47c1-9f8b-0f349ccac1f3" width="220">
</p>


