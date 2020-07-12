package CSCI5308.GroupFormationTool.Survey.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import CSCI5308.GroupFormationTool.Course.ICourse;
import CSCI5308.GroupFormationTool.DBUtil.CreateDatabaseConnection;
import CSCI5308.GroupFormationTool.DBUtil.SqlQueryUtil;
import CSCI5308.GroupFormationTool.Model.Answer;
import CSCI5308.GroupFormationTool.QuestionManager.IQuestion;
import CSCI5308.GroupFormationTool.QuestionManager.QuestionFactory;
import CSCI5308.GroupFormationTool.QuestionManager.QuestionObjectFactory;
import CSCI5308.GroupFormationTool.QuestionManager.DAO.QuestionManagerDaoFactory;
import CSCI5308.GroupFormationTool.Survey.ISurvey;
import CSCI5308.GroupFormationTool.Survey.SurveyFactory;
import CSCI5308.GroupFormationTool.Survey.SurveyObjectFactory;

public class SurveyDaoImpl implements ISurveyDao {

	Logger logger = LogManager.getLogger(SurveyDaoImpl.class);

	@Override
	public ISurvey getSurveyForCourse(ICourse course) {
		PreparedStatement statement = null;
		Connection connection = null;
		ISurvey survey = SurveyFactory.surveyObject(new SurveyObjectFactory());
		try {
			connection = CreateDatabaseConnection.instance().createConnection();
			String selectQuery = SqlQueryUtil.instance().getQueryByKey("surveyForCourse");
			statement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			statement.setInt(1, course.getCourseId());
			ResultSet resultSet = statement.executeQuery();
			ArrayList<IQuestion> surveyQuestions = new ArrayList<IQuestion>();
			if (resultSet.next()) {
				resultSet.beforeFirst();
				while (resultSet.next()) {
					survey.setSurveyId(resultSet.getInt("survey_id"));
					int questionId = resultSet.getInt("question_id");
					IQuestion question = QuestionFactory.questionObject(new QuestionObjectFactory());
					question.setQuestionId(questionId);
					question.setQuestionText(resultSet.getString("question_text"));
					question.setQuestionType(resultSet.getString("question_type"));
					question.setQuestionNo(resultSet.getInt("s_id"));
					question.setCriteria(resultSet.getString("criteria"));
					ArrayList<Answer> answerList = QuestionManagerDaoFactory.instance().fetchQuestionDAO()
							.getOptionsForQuestion(questionId);
					question.setAnswerList(answerList);
					surveyQuestions.add(question);
				}
				survey.setQuestionList(surveyQuestions);
			}
		} catch (SQLException e) {
			logger.error("Exception occured while saving question and answers", e);
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
		return survey;
	}

}
