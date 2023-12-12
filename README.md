![GitHub contributors](https://img.shields.io/github/contributors/laurenthat-metropolia/AI-Form-Builder)
![GitHub repo size](https://img.shields.io/github/repo-size/laurenthat-metropolia/AI-Form-Builder?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/laurenthat-metropolia/AI-Form-Builder?style=flat-square)
![GitHub issues](https://img.shields.io/github/issues/laurenthat-metropolia/AI-Form-Builder)
![GitHub MIT](https://img.shields.io/badge/license-MIT-blue)

![Web Application logo](https://github.com/laurenthat-metropolia/AI-Form-Builder/blob/main/screenshots/background_icon.jpg 'Logo Title Text')


<br />
<div align="center">
  <h1 align="center">Draw2Form</h1>
  <p align="center">
    AI Form Builder
  </p>
</div>

# About the project

Application for converting hand-drawn wireframes into working interfaces

Welcome to Draw2Form AI Form Builder, an Android application that leverages cutting-edge
technologies to streamline image analysis,
text recognition, and data processing. This application is designed to empower users to convert
hand-drawn sketches or
wireframes into interactive forms with ease. Harnessing the power of AI, users with minimal or no
technical
knowledge can create a digital form with utmost ease. The Frontend of application is written in
Kotlin using Jetpack Compose for seamless UI experience and backend is written in TypeScript.
The application was done within a period of seven weeks using scrum-agile methodology to keep track
of the project. Backlogs were tracked and fulfilled using Github project Dashboard. We had weekly
review with the customer regarding the progress of the application. Application is available in both
app and web version with more focus upon the Android version. The task was conducted based upon the
study requirement of the course Mobile Project - Autumn 2023 - Metropolia University Of Applied
Sciences.

## Tech Stacks

- **Prisma**: Prisma is a robust database management tool that enhances data access and management
  within the application.

- **Kotlin Compose**: Kotlin Compose is used for building the modern and intuitive user interface of
  the Android application.

- **Swagger**: Swagger is integrated for API documentation, making it easy for us to understand and
  interact with the backend services.
  Postman was used at the beginning of the project but was eventually replaced by Swagger when we
  changed the usaged of Express to NestJs.

- **Grafana**: Grafana is utilized for monitoring and and logger tool for the application

- **NestJS**: NestJS, built with TypeScript, powers the backend services of the application.

- **Material3**: Material3 is employed to maintain a consistent and visually appealing design
  language across the app.

- **Docker**: Docker is employed for containerization, allowing seamless deployment and scalability
  of the application.

- **Kubernetes**: Kubernetes is used for container orchestration, simplifying the management and
  scaling of Docker containers.

- **Azure Vision Api**: Azure Vision Api is used for Text Detection

## Features

1. **Image Processing**: Users can capture or upload hand-drawn sketches or wireframes for analysis.

2. **AI-Powered Processing**: The application utilizes Azure for text analysis, ChatGPT 3.5 for data
   processing, and ChatGPT 4 for data analysis.

3. **Form Editing**: Users can edit forms generated through AI, add new UI components, and delete
   existing components.

4. **Publish and Share**: Forms can be published and shared using QR codes, allowing easy sharing
   and collaboration.

5. **Form Submission**: Recipients can fill out the forms via QR codes, either anonymously or with
   identity.

6. **Authentication**: Oauth2 Google authentication ensures secure login and access control.

7. **Multilingual Support**: The application supports both Finnish and English languages, based upon
   users language setting

## Dataset and CI/CD Workflow

- **Customized Model**: The dataset was trained using a customized model to enhance accuracy and
  performance. Personal server was used to connect through github runner.

- **GitHub CI/CD Workflow**: The CI/CD workflow on GitHub automates the training of the dataset,
  ensuring continuous improvement.

## Supported Form Components

The application supports various form components:

- Label
- Text
- TextField
- Toggle Switch
- Image
- Checkbox

## GitHub Repositories

The application is divided into two GitHub repositories:

1. [Backend Repo](https://github.com/laurenthat-metropolia/AI-Form-Builder-LLM)
2. [Frontend Repo](https://github.com/laurenthat-metropolia/AI-Form-Builder)

## Getting Started

### Prerequisites

- Android Studio
- Android device or emulator

**Clone the repository:**

   ```bash
   git clone [https://github.com/laurenthat-metropolia/AI-Form-Builder]
  ```

Install [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjw7p6aBhBiEiwA83fGuqT7KA7eHmM5sXJM80gm4mLInuaNEvH5dpfenPSQcvI90ZiLWcroRxoCN9oQAvD_BwE&gclsrc=aw.ds).

- Open Android Studio.

- Click on "Open an existing Android Studio project."

- Navigate to the cloned repository and select the "draw2form" directory.

- Build project for the first time to update dependencies.

### Built With

[![Android][Android.js]][Android-url]
[![Kotlin][Kotlin.js]][Kotlin-url]
[![TypeScript][TypeScript.js]][TypeScript-url]
[![HTML][HTML.js]][HTML-url]
[![Tailwind][Tailwind.js]][Tailwind-url]
[![Docker][Docker.js]][Docker-url]
[![ChatGPT][ChatGPT.js]][ChatGPT-url]
[![Swagger][Swagger.js]][Swagger-url]
[![Grafana][Grafana.js]][Grafana-url]
[![Azure][Azure.js]][Azure-url]
[![Kubernetes][Kubernetes.js]][Kubernetes-url]
[![Prisma][Prisma.js]][Prisma-url]

## Usage

1. Capture or upload a sketch or wireframe.
2. Process the image through object detection, text detection, and form generation.
3. Edit and customize the generated form.
4. Publish and share the form using QR codes.
5. Recipients can fill out the forms via QR codes.
6. Receipants can submit the from anonymously.
7. Form owner gets the insights of filled forms

## Download APK to test the application

#### Release Version

[app/release/app-release.apk](https://github.com/laurenthat-metropolia/AI-Form-Builder/tree/main/app/release)

**Steps**
-  Please use CHROME while browsing the application.
-  Download the apk file
- Press Download anyway (Ignore File might be harmful alert)
- Open the downloaded file and click install
- Unsafe app blocked Pop up window appears in screen
- Press more details and install anyway
- Open the app (default browser should be chrome)
- Enjoy the application
- Please be mindful that the form supports Label, TextFields, ToggleSwitches, Images, Checkbox and Button.


#### Debug Version

[app/debug/app-debug.apk](https://github.com/laurenthat-metropolia/AI-Form-Builder/tree/main/app/debug)

## Application Links

1.[Swagger](https://draw2form.ericaskari.com/api/docs#/)

2.[Grafana](https://logs.ericaskari.com/d/xXpdonSIz/sbma?orgId=1&refresh=5s)

3.[Web Version](https://draw2form.ericaskari.com/)

## Contributors

- [Erik Askari](https://github.com/ericaskari)
- [Shayne Kandagor](https://github.com/shaykandagor)
- [Sebastian Hategan](https://github.com/laurenthat)
- [Binod Panta](https://github.com/frozenfi)

## License

This project is licensed under the MIT License.


[Android.js]: https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white

[Android-url]: https://www.android.com

[Kotlin.js]: https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white

[Kotlin-url]: https://kotlinlang.org

[TypeScript.js]: https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white

[TypeScript-url]: https://www.typescriptlang.org

[HTML.js]: https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white

[HTML-url]: https://html.com

[Tailwind.js]: https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white

[Tailwind-url]: https://tailwindcss.com

[Docker.js]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white

[Docker-url]: https://www.docker.com

[ChatGPT.js]: https://img.shields.io/badge/chatGPT-74aa9c?style=for-the-badge&logo=openai&logoColor=white

[ChatGPT-url]: https://chat.openai.com

[Swagger.js]: https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white

[Swagger-url]: https://swagger.io

[Grafana.js]: https://img.shields.io/badge/grafana-%23F46800.svg?style=for-the-badge&logo=grafana&logoColor=white

[Grafana-url]: https://grafana.com

[Azure.js]: https://img.shields.io/badge/azure-%230072C6.svg?style=for-the-badge&logo=microsoftazure&logoColor=white

[Azure-url]: https://azure.microsoft.com/en-gb/free/ai-services/search/?ef_id=_k_Cj0KCQiA4NWrBhD-ARIsAFCKwWsHx36cPwy5roWz1SiiLjw-bdh7x_goin58mrJowpGjo_vcNFG2Br4aAmrTEALw_wcB_k_&OCID=AIDcmmftanc7uz_SEM__k_Cj0KCQiA4NWrBhD-ARIsAFCKwWsHx36cPwy5roWz1SiiLjw-bdh7x_goin58mrJowpGjo_vcNFG2Br4aAmrTEALw_wcB_k_&gad_source=1&gclid=Cj0KCQiA4NWrBhD-ARIsAFCKwWsHx36cPwy5roWz1SiiLjw-bdh7x_goin58mrJowpGjo_vcNFG2Br4aAmrTEALw_wcB

[Kubernetes.js]: https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white

[Kubernetes-url]: https://kubernetes.io

[Prisma.js]: https://img.shields.io/badge/Prisma-3982CE?style=for-the-badge&logo=Prisma&logoColor=white

[Prisma-url]: https://www.prisma.io
