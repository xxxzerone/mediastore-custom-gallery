# [PRD] MediaStore 기반 커스텀 갤러리 애플리케이션
## 1. 개요

단말기에 저장된 이미지 데이터를 MediaStore API를 통해 조회하고, 사용자가 부드럽게 탐색 및 공유할 수 있는 커스텀 갤러리 UI를 구축합니다. 대용량 데이터 처리를 위해 Paging3를 도입하며, 보안 공유를 위해 FileProvider를 활용합니다.

---
## 2. 핵심 기능 요구사항 (Functional Requirements)

- 사진 쿼리 및 나열: MediaStore를 사용하여 기기 내 모든 사진을 날짜 역순(최신순)으로 조회.
- 페이징 처리: Paging3 라이브러리를 사용하여 스크롤 시 데이터를 청크 단위로 로드하여 메모리 효율성 극대화.
- 상세 정보 보기: 선택한 이미지의 상세 정보(EXIF 데이터: 촬영 날짜, GPS 위치, 파일 크기 등) 표시.
- 이미지 외부 공유: FileProvider와 암시적 인텐트(Implicit Intent)를 사용하여 안전하게 이미지 공유.

---
## 3. 기술 스택 및 아키텍처

- Architecture: Clean Architecture (Domain, Data, Presentation 레이어 분리)
- Pattern: MVVM (Data Binding/State 관리) + MVI (Intent 기반 상태 변화 처리)
- Libraries: Paging3, Glide (이미지 로딩), Hilt (DI), Coroutines & Flow

---
## 4. 상세 설계 및 구현 로직
### 4.1. Clean Architecture 구조
#### 1. Domain Layer:

- Entity: 사진 데이터 모델 (Uri, 이름, 날짜 등).
- Repository Interface: 사진 데이터 소스에 접근하기 위한 추상화 인터페이스.
- UseCase: GetPhotosUseCase (페이징 데이터 스트림 반환).

#### 2. Data Layer:

- PagingSource: MediaStore를 쿼리하여 페이지 단위로 리스트 반환.
- Repository Implementation: Pager를 설정하고 Flow<PagingData<Photo>> 생성.

#### 3. Presentation Layer (MVVM + MVI):

- Intent (User Action): LoadPhotos, ClickPhoto, SharePhoto.
- State (UI State): PhotoListState, PhotoDetailState.
- ViewModel: 사용자의 Intent를 수신하여 로직을 실행하고 유일한 상태(State)를 업데이트하여 UI에 노출.

### 4.2. Paging3 연동 로직

- MediaStore 쿼리 시 OFFSET과 LIMIT을 사용하여 SQL 쿼리 최적화.
- PagingDataAdapter를 사용하여 리사이클러뷰의 리스트 업데이트 및 로딩 상태 처리.

### 4.3. 상세 정보 (EXIF) 및 공유

- EXIF: ExifInterface 라이브러리를 사용하여 ContentUri로부터 메타데이터 추출.
- FileProvider: res/xml/file_paths.xml 설정 및 FileProvider.getUriForFile()을 통한 보안 URI 생성.

---
## 5. UI/UX 디자인 가이드

- Main View: 그리드 레이아웃 (3단 구성), 무한 스크롤 적용.
- Detail View: 이미지 풀스크린 미리보기 및 하단 시트(Bottom Sheet)를 통한 EXIF 정보 출력.
- Share: 시스템 표준 공유 시트 호출.

---
## 6. 예외 처리 및 고려 사항

- 권한 관리: Android 13(API 33) 이상의 READ_MEDIA_IMAGES 권한 및 하위 버전의 READ_EXTERNAL_STORAGE 대응.
- 성능 최적화: 이미지 썸네일 생성 시 디스크/메모리 캐싱 전략 수립 (Glide 활용).
- 빈 상태 처리: 기기에 사진이 없을 경우 가이드 화면 표시.