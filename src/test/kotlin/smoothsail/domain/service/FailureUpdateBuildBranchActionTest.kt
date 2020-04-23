package smoothsail.domain.service

import org.junit.Test
import org.mockito.Mockito
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.domain.JobBuildDetails

internal class FailureUpdateBuildBranchActionTest: UpdateBuildBranchActionTest() {
    @Test
    fun `Entry properly updated on failure, and jenkins notified to abort dependent ones`() {
        val original = BuildBranchDetails(id = 1)
        val next = BuildBranchDetails(id = 2)
        val nextnext = BuildBranchDetails(id = 3)
        val jobName = "job"
        val buildNumber = 10L
        val job1 = JobBuildDetails(id = 1)
        val job2 = JobBuildDetails(id = 2)
        Mockito.`when`(buildBranchDetailsRepository.findFirstByPreviousBuildBranchDetailsId(original.id)).thenReturn(next)
        Mockito.`when`(buildBranchDetailsRepository.findFirstByPreviousBuildBranchDetailsId(next.id)).thenReturn(nextnext)
        Mockito.`when`(jobBuildBranchDetailsRepository.findByBuildBranchDetailsId(next.id)).thenReturn(listOf(job1))
        Mockito.`when`(jobBuildBranchDetailsRepository.findByBuildBranchDetailsId(nextnext.id)).thenReturn(listOf(job2))
        failureUpdateBuildBranchAction.execute(original, jobName, buildNumber)
        Mockito.verify(buildBranchDetailsRepository, Mockito.times(1)).save(original.copy(status = BuildBranchStatus.FAILURE))
        Mockito.verify(jenkinsJobAbortionOperation, Mockito.times(1)).abort(job1)
        Mockito.verify(jenkinsJobAbortionOperation, Mockito.times(1)).abort(job2)
    }
}