package CSCI5308.GroupFormationTool.ToolAdmin.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import CSCI5308.GroupFormationTool.Course.Course;
import CSCI5308.GroupFormationTool.DBUtil.CreateDatabaseConnection;
import CSCI5308.GroupFormationTool.DBUtil.SqlQueryUtil;

public class ViewCoursesImpl implements IViewCourses {

	private Logger logger = LogManager.getLogger(ViewCoursesImpl.class);

	@Override
	public ArrayList<Course> getCourseList() {

		Connection connection = null;
		PreparedStatement statement = null;
		ArrayList<Course> courseData = new ArrayList<>();

		try {
			connection = CreateDatabaseConnection.instance().createConnection();
			String selectQuery = SqlQueryUtil.instance().getQueryByKey("allCourses");
			statement = connection.prepareStatement(selectQuery);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Course tempCourseObj = new Course();
				tempCourseObj.setCourseCode(resultSet.getString("course_code"));
				tempCourseObj.setCourseName(resultSet.getString("course_name"));
				tempCourseObj.setCourseId(resultSet.getInt("course_id"));
				courseData.add(tempCourseObj);
			}
		} catch (SQLException e) {
			logger.error("Exception occured while fetching the course list", e);
		} finally {
			try {
				if (null != statement) {
					statement.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Exception occured while closing connection/statement", e);
			}
		}

		return courseData;
	}

}