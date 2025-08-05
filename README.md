# 🗺️ 지나다 (Jinada)
**"이제 할 일을 잊고 지나치지 마세요! 당신이 지나가는 길, 할 일을 알려드려요."**

**지나다**는 위치기반으로 메모를 등록하고, 근접한 위치를 지나갈 때 해당 메모에 대한 알림을 주는 Android 앱입니다.

<p align="center">
  <img src="./assets/logo.png" alt="지나다 로고" width="200"/>
</p>

---

## 📝 프로젝트 개요
- **앱 이름**: 지나다 (Jinada)
- **개발 기간**: 25.06 ~ 진행중
- **플랫폼**: Android
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
| 아키텍처     | MVVM |
| UI            | Jetpack Compose |
| 데이터베이스  | Firebase Firestore |
| 위치 서비스  | Fused Location Provider API, Geofencing API, ActivityRecognition API, Naver Map API |
| 알림         | Notification API |
| 기타         | Coroutines, Flow |

---

## ✨ 주요 기능

### 1️⃣ 위치 기반 메모 등록
- 지도에서 특정 위치를 선택하고 해당 위치에서 수행할 할 일을 등록
- 위치별로 다수의 할 일 관리 가능

<p align="center">
  <img src="./assets/mockup_location.png" alt="위치 등록 화면" width="300" style="border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);"/>
</p>

---

### 2️⃣ 위치 도달 알림
- 사용자가 지정한 위치 근처에 도달 시 자동 알림
- 알림에서 바로 할 일 완료 처리 가능

<p align="center">
  <img src="./assets/mockup_notification.png" alt="알림 화면" width="300" style="border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);"/>
</p>

---

### 3️⃣ 할 일 리스트 관리
- 등록된 위치별 할 일을 한눈에 확인
- 완료/미완료 상태 관리 및 수정 가능

<p align="center">
  <img src="./assets/mockup_todolist.png" alt="할 일 관리 화면" width="300" style="border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);"/>
</p>

---

### 4️⃣ 마이페이지
- 알림 설정 및 메모 감지 범위 설정 기능

<p align="center">
  <img src="./assets/mockup_mypage.png" alt="마이페이지 화면" width="300" style="border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);"/>
</p>

---

### 5️⃣ 통계
- 메모에 대한 전체, 월간, 주간별 통계 정보 제공
- 미진행 메모 리스트 제공 및 처리 기능


---

## 🚀 향후 계획
- 지도 클러스터링 및 위치 기반 자동 추천

---
