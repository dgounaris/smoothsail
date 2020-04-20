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

internal class UpdateBuildBranchActionTest {
    private val buildBranchDetailsRepository = mock(BuildBranchDetailsRepository::class.java)
    private val jobBuildBranchDetailsRepository = mock(JobBuildDetailsRepository::class.java)
    private val jenkinsJobAbortionOperation = mock(JenkinsJobAbortionOperation::class.java)
    private val successUpdateBuildBranchAction = SuccessUpdateBuildBranchAction(buildBranchDetailsRepository)
    private val failureUpdateBuildBranchAction = FailureUpdateBuildBranchAction(
        buildBranchDetailsRepository, jobBuildBranchDetailsRepository, jenkinsJobAbortionOperation
    )

    @Test
    fun `Entry properly updated on success`() {
        val original = BuildBranchDetails()
        successUpdateBuildBranchAction.execute(original, null, null)
        Mockito.verify(buildBranchDetailsRepository, Mockito.times(1)).save(original.copy(status = BuildBranchStatus.SUCCESS))
    }

    @Test
    fun `Entry properly updated on failure, and jenkins notified to abort dependent ones`() {
        val original = BuildBranchDetails(id = 1)
        val next = BuildBranchDetails(id = 2)
        val nextnext = BuildBranchDetails(id = 3)
        val jobName = "job"
        val buildNumber = 10L
        val job1 = JobBuildDetails(id = 1)
        val job2 = JobBuildDetails(id = 2)
        `when`(buildBranchDetailsRepository.findFirstByPreviousBuildBranchDetailsId(original.id)).thenReturn(next)
        `when`(buildBranchDetailsRepository.findFirstByPreviousBuildBranchDetailsId(next.id)).thenReturn(nextnext)
        `when`(jobBuildBranchDetailsRepository.findByBuildBranchDetailsId(next.id)).thenReturn(listOf(job1))
        `when`(jobBuildBranchDetailsRepository.findByBuildBranchDetailsId(nextnext.id)).thenReturn(listOf(job2))
        failureUpdateBuildBranchAction.execute(original, jobName, buildNumber)
        Mockito.verify(buildBranchDetailsRepository, Mockito.times(1)).save(original.copy(status = BuildBranchStatus.FAILURE))
        Mockito.verify(jenkinsJobAbortionOperation, Mockito.times(1)).abort(job1)
        Mockito.verify(jenkinsJobAbortionOperation, Mockito.times(1)).abort(job2)
    }
}