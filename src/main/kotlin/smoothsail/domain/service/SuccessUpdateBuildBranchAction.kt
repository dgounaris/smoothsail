package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.jenkins.JenkinsJobAbortionOperation
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.repository.JobBuildDetailsRepository

@Component
class SuccessUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UpdateBuildBranchAction {
    override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
        val updated = buildBranchDetails.copy(
                status = BuildBranchStatus.SUCCESS
        )
        buildBranchDetailsRepository.save(updated)
        // TODO NEED TO MERGE
    }
}