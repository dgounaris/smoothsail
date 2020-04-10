package smoothsail.domain

enum class BuildBranchStatus {
  PENDING,
  STARTED,
  SUCCESS,
  FAILURE,
  MERGED,
  CONFLICT,
  ABORTED
}