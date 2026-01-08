# MediaStore 사진 브라우저
단말기에 저장된 모든 사진을 날짜순으로 쿼리하여 커스텀 갤러리 구현

- 사진을 선택하면 해당 사진의 상세 정보(EXIF 데이터 - 찍은 날짜, 위치 등)을 보여줌
- Paging3를 연동하여 많은 데이터를 스크롤 할 때마다 조금씩 불러오도록 구현
- FileProvider로 이미지 공유
  - 암시적 인덴트(공유)

## References
- [Paging3 공식 문서](https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=ko)
- [FileProvider 공식 문서](https://developer.android.com/training/secure-file-sharing?hl=ko)

## Preview
<img width="300" height="600" alt="Screenshot_20260109_000807" src="https://github.com/user-attachments/assets/98933032-a042-4224-92b4-b2867d3e7a88" />
<img width="300" height="600" alt="Screenshot_20260109_000828" src="https://github.com/user-attachments/assets/0161417a-f066-485a-a91d-de514f5cffa2" />
<img width="300" height="600" alt="Screenshot_20260109_000912" src="https://github.com/user-attachments/assets/c0ae5b7e-2be3-4b70-a9fa-8aa9d08a8b0e" />
<img width="300" height="600" alt="Screenshot_20260109_000847" src="https://github.com/user-attachments/assets/c11f81ba-f40b-46dd-ab0d-5f5fefb0619d" />
