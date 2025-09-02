# 감정 기반 가계부 애플리케이션 스펜더

# 프로젝트 소개
* 스펜더는 소비 기록에 감정 기록을 결합하여 소비 습관을 성찰할 수 있도록 도와주는 가계부 애플리케이션입니다.
* 소비 기록에 감정 태그를 추가하여 소비의 타당성을 주관적으로 평가하고, 태그 기록에 기반하여 한 달의 소비를 돌아볼 수 있습니다.
* 현재 위치한 달의 실시간 통계와 월별 리포트를 제공합니다. 월별 리포트에서는 Ai를 활용한 제안사항 및 요약을 받아볼 수 있게 합니다.
* 한 달의 예산을 설정할 수 있고, 비정기적인 수입을 기록할 수 있습니다.
* 예산 대비 지출 추이를 확인할 수 있습니다.
* 영수증 사진을 등록해 간편하게 소비기록을 관리할 수 있습니다.

# 팀원 구성
| 강태훈 | 정가은 | 이수지 | 이다영 |
| - | - | - | - |
| 팀장 | 부팀장 | 팀원 | 팀원 |
| 마이페이지, 홈, 지출/수입 등록 관련 기능 | DB(firebase storage) 설정, 통계 관련 기능 | firebase cloud functions 설정, 리포트 관련 기능 | 소셜 로그인, firebase auth 설정, 로그인/온보딩 관련 기능 |
| [@xogns950](https://github.com/xogns950) | [@esfjge](https://github.com/EunaJ99/EunaJ99) | [@jessica3579](https://github.com/jessica3579) | [@urhelp](https://github.com/urhelp) |

# 주요 화면
| 홈 | 통계 | 리포트 | 지출 등록 |
| - | - | - | - |
| <img width="150" height="400" alt="Image" src="https://github.com/user-attachments/assets/da164496-8560-4cbf-a7ff-d8d59bab9b04" /> | <img width="150" height="400" alt="Image" src="https://github.com/user-attachments/assets/8b1a06c8-d77c-4d63-bf53-81feac996e7f" /> | <img width="150" height="400" alt="Image" src="https://github.com/user-attachments/assets/365df8df-51a9-444c-92ed-23e609293558" /> | <img width="150" height="400" alt="Image" src="https://github.com/user-attachments/assets/a7bf8c4e-3afe-4af7-a304-d9fb6d12bec6" /> |


# 개발 기간 및 작업 관리
### 개발 기간
* 기획 및 업무 분장: 2025-07-24 ~ 2025-07-28
* UI 개발: 2025-07-28 ~ 2025-08-05
* 기능 개발: 2025-08-01 ~ 2025-09-02

### 작업 관리
* 디자인은 Figma에서 관리, 기획 기간에 화면 명세서 개발 완료
* 각자 맡은 부분을 독립적으로 개발, git branch를 활용하여 develop branch에서 통합
* 매일 회의를 진행하여 당일의 작업 목표 및 속도를 확인, Notion에 회고록 작성

# 개발 환경
* 사용 언어: Kotlin, 일부 JavaScript
* IDE: Android Studio
* 협업 툴: Notion, Discord, Figma, Zoom Workspace
* 테스트 기기: Android Studio AVD Pixel 3

## 사용 라이브러리
* 소셜 로그인: Google, Kakao, Naver (+ Firebase Authentication, Firebase Cloud Functions)
* 알람 시스템: Firebase Cloud Messaging
* DB: Firebase Storage (+ Firebase Cloud Functions)
* 그래프: MPAndroidChart

# 주요 기능
### 🤝 소셜 
* 30분간 유효한 친구 코드를 통해 다른 유저와 친구를 맺을 수 있습니다.
* 친구의 프로필과 간단한 통계를 조회할 수 있습니다.
* 매월 예산 대비 지출에 따라 티어가 변경됩니다.

### 📊 통계와 리포트 
* 예산 대비 지출을 시각화하여 볼 수 있습니다.
* 캘린더 및 그래프로 한달 간의 지출을 시각화하여 볼 수 있습니다.
* 한 달마다 리포트를 통해 ai 피드백을 받을 수 있습니다.

### 💰 지출 및 수입 등록
* 지출 및 수입을 직접 또는 자동으로 등록할 수 있습니다.
* 영수증을 통한 지출등록이 가능합니다.
* 정기지출로 자동으로 지출을 등록할 수 있습니다.
* 지출에 감정태그를 등록할 수 있습니다.

### ⚙️ 계정 관리 및 설정
* 사용자의 계정 정보를 조회하고 닉네임과 프로필 사진을 변경할 수 있습니다.
* 수입 및 지출 카테고리를 설정할 수 있습니다.
* 예산과 정기지출을 조회, 수정할 수 있습니다.
* 알림 설정, 회원 탈퇴, 로그아웃이 가능합니다.

### 📱 위젯
* 두 종류의 위젯을 제공합니다. 
