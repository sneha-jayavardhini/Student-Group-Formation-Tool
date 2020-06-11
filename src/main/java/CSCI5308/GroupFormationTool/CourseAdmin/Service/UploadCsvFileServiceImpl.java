package CSCI5308.GroupFormationTool.CourseAdmin.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import CSCI5308.GroupFormationTool.CourseAdmin.DAO.CourseAssociationDAO;
import CSCI5308.GroupFormationTool.CourseAdmin.DAO.CourseAssociationDAOImpl;
import CSCI5308.GroupFormationTool.Model.Student;
import CSCI5308.GroupFormationTool.Model.User;
import CSCI5308.GroupFormationTool.Profile.DAO.UserDao;
import CSCI5308.GroupFormationTool.Profile.DAO.UserDaoImpl;
import CSCI5308.GroupFormationTool.Utilities.ApplicationConstants;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class UploadCsvFileServiceImpl implements UploadCsvFileService {

	private String resMessage;
	private Boolean resStatus;
	private List<Student> resStudentList;

	@Override
	public boolean uploadCsvFile(MultipartFile file, Integer courseId, String courseCode, String courseName) {

		UserDao userDao = new UserDaoImpl();
		GetStudentListService getStudentListService = new GetStudentListServiceImpl();
		SendInvitationEmailService sendEmailService = new SendInvitationEmailServiceImpl();
		CourseAssociationDAO courseAssociationDao = new CourseAssociationDAOImpl();

		if (file.isEmpty()) {
			resMessage = ApplicationConstants.FILE_EMPTY;
			resStatus = ApplicationConstants.UPLOAD_STATUS_FALSE;
		} else {
			try {
				List<Student> students = parseCsv(file);

				ArrayList<Integer> userIdsFromCourseAssociation = courseAssociationDao.getUserID(courseId);
				ArrayList<User> userList = userDao.getUserByUserID(userIdsFromCourseAssociation);

				List<Student> newToCourseList = getStudentListService.getNewToCourseStudentList(students, userList);

				ArrayList<User> allUserList = userDao.getAll();
				List<Student> newToPortalList = getStudentListService.getNewToPortalStudentList(newToCourseList,
						allUserList);

				userDao.addUser(newToPortalList);

				ArrayList<Integer> userIdsFromUser = userDao.getUserID(newToCourseList);
				courseAssociationDao.addByUserID(userIdsFromUser, courseId);

				ArrayList<String> passwordFromUser = userDao.getPassword(newToPortalList);

				sendEmailService.sendUserInvitationEmail(newToPortalList, passwordFromUser);
				sendEmailService.sendCourseInvitationEmail(newToCourseList, courseCode, courseName);
				resMessage = ApplicationConstants.FILE_UPLOADED;
				resStatus = ApplicationConstants.UPLOAD_STATUS_TRUE;
				resStudentList = newToCourseList;
			} catch (Exception ex) {
				ex.printStackTrace();
				resMessage = ApplicationConstants.FILE_ERROR;
				resStatus = ApplicationConstants.UPLOAD_STATUS_FALSE;
			}
		}

		return true;
	}

	@Override
	public List<Student> parseCsv(MultipartFile file) {

		List<Student> students = null;

		try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			CsvToBean<Student> csvToBean = new CsvToBeanBuilder<Student>(reader).withType(Student.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			students = csvToBean.parse();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return students;
	}

	@Override
	public String getMessage() {
		return resMessage;
	}

	@Override
	public Boolean getStatus() {
		return resStatus;
	}

	@Override
	public List<Student> getStudentList() {
		return resStudentList;
	}
}
