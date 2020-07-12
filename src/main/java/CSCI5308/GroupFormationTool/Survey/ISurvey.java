package CSCI5308.GroupFormationTool.Survey;

import java.util.List;

import CSCI5308.GroupFormationTool.Course.ICourse;
import CSCI5308.GroupFormationTool.QuestionManager.IQuestion;
import CSCI5308.GroupFormationTool.Survey.Service.ISurveyService;

public interface ISurvey {
	public int getSurveyId();

	public void setSurveyId(int surveyId);

	public List<IQuestion> getQuestionList();

	public void setQuestionList(List<IQuestion> questionList);

	public ISurvey getSurveyForCourse(ICourse course, ISurveyService surveyService);

	public String getCourseId();

	public void setCourseId(String courseId);

	public boolean isPublished();

	public void setPublished(boolean published);

	public int getGroupSize();

	public void setGroupSize(int groupSize);
}
