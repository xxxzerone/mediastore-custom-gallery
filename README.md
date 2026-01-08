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
