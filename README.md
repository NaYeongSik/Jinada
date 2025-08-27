# 🗺️ 지나다 (Jinada)
**"이제 할 일을 잊고 지나치지 마세요! 당신이 지나가는 길, 할 일을 알려드려요."**

**지나다**는 위치기반으로 메모를 등록하고, 근접한 위치(500m)를 지나갈 때 해당 메모에 대한 자동으로 알림을 주는 Android 앱입니다.

<img width="495" height="482" alt="image" src="https://github.com/user-attachments/assets/8ff0c068-d221-488c-bc29-753529f2b9f2" />


---

## 📝 프로젝트 개요
- **앱 이름**: 지나다 (Jinada)
- **개발 기간**: 25.06 ~ 마켓 등록 준비중
- **플랫폼**: Android
- **개발 인원**: 1인 (나영식: 기획,디자인,개발)
- **개발 언어**: Kotlin
- **개발 환경**: Android Studio
---

## 🎯 기획 의도
**지나다(Jinada)** 는 일상생활에서 바쁜 나머지 할 일을 자주 깜빡하고 지나치는 사람들을 위한 위치 기반 메모 알림 앱입니다.

"퇴근길에 마트에 들러서 우유 사기", "회사 근처 우체국에서 택배 보내기" 와 같이 특정 장소에서 해야 할 일들을 간단하게 메모하고 위치를 등록해두세요. 사용자가 해당 위치에 가까워지면 **지나다**가 알아서 알림을 보내주어, 더 이상 중요한 일을 잊지 않도록 도와줍니다.

사용자는 그저 메모에 위치 정보를 포함하는 것만으로, 해당 할 일에 대해 계속 신경 쓰지 않아도 가장 적절한 순간에 알림을 받아볼 수 있습니다.

---

## 🛠 사용 기술
| 분야          | 기술 스택 |
|---------------|-----------|
| 개발 언어     | Kotlin |
| 아키텍처     | MVVM, Hilt |
| UI            | Jetpack Compose Material3 UI/UX|
| Networking  | Retrofit2, Moshi, Firebase Firestore |
| Local Storage  | DataStore |
| Location  | Fused Location Provider API, Geofencing API, ActivityRecognition API |
| Map     | Naver Map |
|  Core Component | **Service & Broadcast Receiver 를 이용한 근접 접근 자동 알림 구현** |
| Tools         | Github, Figma |

---

## ✨ 주요 기능

### 1️⃣ 위치 기반 메모 등록 및 관리
- 지도에서 메모 할 위치를 선택하고 해당 위치에서 수행할 할 일을 등록
- 인접한 위치의 메모를 지도에 마커로 표시
- 등록된 메모를 날짜별로 확인하여 관리 가능
- 완료/미완료 상태 관리 및 수정 가능

| 위치기반 메모 생성 |  장소 검색 |
|--------------------|--------------------|
| <img src="https://github.com/user-attachments/assets/703f33e9-5c40-449e-bf21-eedf429a5d63" width="300"/> | <img src="https://github.com/user-attachments/assets/0b37a912-205a-4a15-9c87-f80704725c47" width="300"/> |
| 네이버 지도를 길게 눌러 위치정보를 확인하고<br>메모를 생성합니다. | 검색바를 통한 장소 검색과 검색 결과를 메모로<br> 등록 할 수 있습니다.  |

| 메츠

---
