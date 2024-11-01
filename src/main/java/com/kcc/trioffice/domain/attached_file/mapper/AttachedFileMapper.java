package com.kcc.trioffice.domain.attached_file.mapper;

import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileDetailInfo;
import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileInfo;
import com.kcc.trioffice.domain.attached_file.dto.response.ImageInfo;
import com.kcc.trioffice.global.image.dto.response.S3UploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AttachedFileMapper {

    int saveAttachedFile(Long chatId, Long employeeId, S3UploadFile s3UploadFile);

    Optional<S3UploadFile> getAttachedFileByChatId(Long chatId);

    List<AttachedFileInfo> getAttachedFile(Long chatRoomId, int limit, int offset, String searchType, List<String> tags);

    List<ImageInfo> getImages(Long chatRoomId, String searchType, List<String> tags);

    Optional<S3UploadFile> getAttachedFileByFileId(Long fileId);

    List<AttachedFileDetailInfo> getAllAttachedFile(Long currentEmployeeId, String extensions, Long senderId, String startDate, String endDate,
                                                    String keyword, Long offset, Long limit);
}
