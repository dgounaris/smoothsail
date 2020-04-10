package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.domain.JobBuildDetails
import smoothsail.operations.jenkins.JenkinsJobAbortionOperation
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.repository.JobBuildDetailsRepository

@Component
class UpdateBuildBranchActionSelectionStrategy(
    private val successUpdateBuildBranchAction: SuccessUpdateBuildBranchAction,
    private val failureUpdateBuildBranchAction: FailureUpdateBuildBranchAction,
    private val nullUpdateBuildBranchAction: NullUpdateBuildBranchAction
) {

  fun select(updatedStatus: BuildBranchStatus) =
    when (updatedStatus) {
      BuildBranchStatus.SUCCESS -> successUpdateBuildBranchAction
      BuildBranchStatus.FAILURE -> failureUpdateBuildBranchAction
      else -> nullUpdateBuildBranchAction
    }
}

interface UpdateBuildBranchAction {
  fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?)
}

@Component
class StartedUpdateBuildBranchAction(
    private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
    val updated = buildBranchDetails.copy(
        status = BuildBranchStatus.STARTED
    )
    buildBranchDetailsRepository.save(updated)
  }
}

@Component
class SuccessUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
      val updated = buildBranchDetails.copy(
            status = BuildBranchStatus.SUCCESS
      )
      buildBranchDetailsRepository.save(updated)
  }
}

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
      getJobsToAbort(buildBranchDetails).map {
        jenkinsJobAbortionOperation.abort(it)
      }
  }

  private fun getJobsToAbort(failedBuildBranch: BuildBranchDetails) =
    getBuildBranchesToAbort(failedBuildBranch).asSequence()
        .map { jobBuildDetailsRepository.findByBuildBranchDetailsId(it.id).single() }
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

@Component
class NullUpdateBuildBranchAction: UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
    // do nothing
  }
}