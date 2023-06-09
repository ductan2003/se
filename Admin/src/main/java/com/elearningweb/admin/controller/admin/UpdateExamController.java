package com.elearningweb.admin.controller.admin;

import com.elearningweb.library.dto.CategoryDto;
import com.elearningweb.library.dto.ExamDto;
import com.elearningweb.library.service.impl.ExamServiceImpl;
import com.elearningweb.library.service.impl.FileServiceImpl;
import com.elearningweb.library.util.StreamUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
/*
FOR ADMIN ONLY
 */
@RestController
@RequestMapping("/admin")
public class UpdateExamController {

    @Autowired
    private ExamServiceImpl examService;
    @Autowired
    private FileServiceImpl fileService;
    @Value("${project.image}")
    private String path;

    //1.Get all exams
    @GetMapping("/exams/all")
    public ResponseEntity<?> listExams() {

        List<ExamDto> list = examService.findAllExams();
        Map<Object, Object> map = Map.of("total", list.size(), "listPost", list);
        return ResponseEntity.ok(map);
    }
    //2.Get exam by id
    @GetMapping("/exams/id={id}")
    public ExamDto getExamById(@PathVariable("id") long id) {
        return examService.findById(id);
    }
    //3.Get all by category
    @GetMapping("/exams/{category}")
    public ResponseEntity<?> listExamsByCategory(@PathVariable("category") String category) {
        List<ExamDto> list = examService.findAllExamsByCategory(category);
        Map<Object, Object> map = Map.of("total", list.size(), "listPost", list);
        return ResponseEntity.ok(map);
    }
    //4. Get all by year
    @GetMapping("/exams/year={year}")
    public ResponseEntity<?> getExamByYear(@PathVariable("year") String year) {
        List<ExamDto> list = examService.findAllByYear(year);
        Map<Object, Object> map = Map.of("total", list.size(), "listPost", list);
        return ResponseEntity.ok(map);
    }
    //5. Get all by year and category
    @GetMapping("/exams/year={year}/{category}")
    public ResponseEntity<?> getExamByYearAndCategory(@PathVariable("year") String year,
                                                      @PathVariable("category") String category) {
        List<ExamDto> list = examService.findAllByYearAndCategory(year, category);
        Map<Object, Object> map = Map.of("total", list.size(), "listPost", list);
        return ResponseEntity.ok(map);
    }
    //6. Get all by category and id
    @GetMapping("exams/{category}/id={id}")
    public ExamDto getExamByCategoryAndId(@PathVariable("category") String category,
                                          @PathVariable("id") long id) {
        return examService.findByCategoryAndId(category, id);
    }
    //7. Get all by category and id and year
    @GetMapping("/exams/year={year}/{category}/id={id}")
    public ExamDto getExamByYearAndCategoryAndId(@PathVariable("category") String category,
                                                 @PathVariable("id") long id,
                                                 @PathVariable("year") String year) {
        return examService.findByYearAndCategoryAndId(year, category, id);
    }
    @GetMapping(value = "/exams/file/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadFile(@PathVariable("fileName") String fileName,
                             HttpServletResponse response) throws Exception {
        InputStream resource = this.fileService.getResource(path, fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
    //ADD EXAM
    @PostMapping(value = "/exams/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ExamDto createExam(@RequestPart String title,
                              @RequestPart String category,
                              @RequestPart String description,
                              @RequestPart String year,
                              @RequestPart MultipartFile fileQuestion,
                              @RequestPart MultipartFile fileImage
    ) throws Exception {
        ExamDto examDto = new ExamDto();
        saveExam(fileQuestion, fileImage, examDto);
        examDto.setTitle(title);
        examDto.setDescription(description);
        examDto.setYear(year);
        examDto.setCategory(new CategoryDto(null, category));
        return examService.save(examDto);
    }

    //UPDATE EXAM
    @PutMapping(value = "/exams/update/id={id}")
    public ExamDto updateExam(@RequestPart String title,
                              @RequestPart String category,
                              @RequestPart String description,
                              @RequestPart String year,
                              @RequestPart MultipartFile fileQuestion,
                              @RequestPart MultipartFile fileImage,
                              @PathVariable("id") long id) throws Exception {
        ExamDto examDto = examService.findById(id);
        if (examDto == null) {
            return null;
        } else {
            saveExam(fileQuestion, fileImage, examDto);

            examDto.setTitle(title);
            examDto.setDescription(description);
            examDto.setYear(year);
            examDto.setCategory(new CategoryDto(null, category));
        }
        return examService.save(examDto);
    }

    //DELETE EXAM
    @DeleteMapping(value = "/exams/delete/id={id}")
    public void deleteExam(@PathVariable("id") long id) {
        examService.delete(id);
    }

    //SAVE FUCTION
    private void saveExam(
                          MultipartFile fileQuestion,
                          MultipartFile fileImage,
                          ExamDto examDto) throws Exception {

        fileService.save(fileQuestion, FileServiceImpl.fileQuestionPath);
        examDto.setFileQuestion(FileServiceImpl.path.toString());

        String fileName = fileService.updateFile(path, fileImage);
        examDto.setFileImage(fileName);
    }
}