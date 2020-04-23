package smoothsail.domain.service

import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.domain.JobBuildDetails
import smoothsail.operations.jenkins.JenkinsJobAbortionOperation
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.repository.JobBuildDetailsRepository

internal open class UpdateBuildBranchActionTest {
    protected val buildBranchDetailsRepository = mock(BuildBranchDetailsRepository::class.java)
    protected val jobBuildBranchDetailsRepository = mock(JobBuildDetailsRepository::class.java)
    protected val jenkinsJobAbortionOperation = mock(JenkinsJobAbortionOperation::class.java)
    protected val successUpdateBuildBranchAction = SuccessUpdateBuildBranchAction(buildBranchDetailsRepository)
    protected val failureUpdateBuildBranchAction = FailureUpdateBuildBranchAction(
        buildBranchDetailsRepository, jobBuildBranchDetailsRepository, jenkinsJobAbortionOperation
    )
}