package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.git.api.GithubApi
import smoothsail.operations.git.api.GithubJGitApi
import smoothsail.operations.jenkins.JenkinsJobAbortionOperation
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.repository.JobBuildDetailsRepository

@Component
class SuccessUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository,
        private val githubApi: GithubJGitApi
): UpdateBuildBranchAction {
    override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
        val updated = buildBranchDetails.copy(
                status = BuildBranchStatus.SUCCESS
        )
        buildBranchDetailsRepository.save(updated)
        githubApi.mergeAndPush(buildBranchDetails.repository, buildBranchDetails.currentBuildBranchName, buildBranchDetails.targetBranch)
    }
}