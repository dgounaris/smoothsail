package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.jenkins.JenkinsJobAbortionOperation
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.repository.JobBuildDetailsRepository

@Component
class FailureUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository,
        private val jobBuildDetailsRepository: JobBuildDetailsRepository,
        private val jenkinsJobAbortionOperation: JenkinsJobAbortionOperation
): UpdateBuildBranchAction {
    override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
        val updated = buildBranchDetails.copy(
                status = BuildBranchStatus.FAILURE
        )
        buildBranchDetailsRepository.save(updated)
        handleNecessaryAborts(updated)
    }

    private fun handleNecessaryAborts(buildBranchDetails: BuildBranchDetails) {
        val jobsToAbortByBuildBranches = getJobsToAbortByBuildBranches(buildBranchDetails)
        jobsToAbortByBuildBranches.map {
            jenkinsJobAbortionOperation.abort(it.second)
        }
        val updatedAborted = jobsToAbortByBuildBranches.map {
            it.first.copy(
                    status = BuildBranchStatus.ABORTED
            )
        }
        buildBranchDetailsRepository.saveAll(updatedAborted)
    }

    private fun getJobsToAbortByBuildBranches(failedBuildBranch: BuildBranchDetails) =
            getBuildBranchesToAbort(failedBuildBranch).asSequence()
                    .map { it to jobBuildDetailsRepository.findByBuildBranchDetailsId(it.id).single() }
                    .toList()

    private fun getBuildBranchesToAbort(failedBuildBranch: BuildBranchDetails): List<BuildBranchDetails> {
        val buildBranches = mutableListOf<BuildBranchDetails>()
        var chainedBuildBranch = buildBranchDetailsRepository.findFirstByPreviousBuildBranchDetailsId(failedBuildBranch.id)
        while (chainedBuildBranch != null) {
            buildBranches.add(chainedBuildBranch)
            chainedBuildBranch = buildBranchDetailsRepository.findFirstByPreviousBuildBranchDetailsId(chainedBuildBranch.id)
        }
        return buildBranches
    }

}