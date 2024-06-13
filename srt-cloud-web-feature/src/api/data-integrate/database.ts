import service from '@/utils/request'

// Mine
export const useDatasourceApi = (id: number) => {
	return service.get('/data-integrate/datasource/' + id)
}
// 保留
export const useDatabaseApi = (id: number) => {
	return service.get('/data-integrate/database/' + id)
}

// Mine
export const useDataSourceApi_v2 = (id: number) => {
	return service.get('/data-integrate/datasource/' + id)
}
// Mine
export const useDatabaseApi_v2 = (id: number) => {
	return service.get('/data-integrate/database/' + id)
}
// Mine
export const useDatatableApi_v2 = (id: number) => {
	return service.get('/data-integrate/datatable/' + id)
}

// Mine
export const useDatasourceSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/data-integrate/datasource', dataForm)
	} else {
		return service.post('/data-integrate/datasource', dataForm)
	}
}

// 保留？？？
export const useDatabaseSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/data-integrate/database', dataForm)
	} else {
		return service.post('/data-integrate/database', dataForm)
	}
}

// Mine
export const useDataSourceSubmitApi_v2 = (dataForm: any) => {
	if (dataForm.id) {
		console.log('put /data-integrate/datasource')
		return service.put('/data-integrate/datasource', dataForm)
	} else {
		console.log('post /data-integrate/datasource')
		return service.post('/data-integrate/datasource', dataForm)
	}
}
// Mine
export const useDatabaseSubmitApi_v2 = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/data-integrate/database', dataForm)
	} else {
		return service.post('/data-integrate/database', dataForm)
	}
}
// Mine
export const useDatatableSubmitApi_v2 = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/data-integrate/datatable', dataForm)
	} else {
		return service.post('/data-integrate/datatable', dataForm)
	}
}

// 要
// export const testOnline = (dataForm: any) => {
// 	return service.post('/data-integrate/database/test-online', dataForm)
// }
// Mine
export const testOnline_datasource = (dataForm: any) => {
	return service.post('/data-integrate/datasource/test-online', dataForm)
}
// Mine
export const testOnline_database = (dataForm: any) => {
	return service.post('/data-integrate/database/test-online', dataForm)
}

// 保留？？？
export const getTablesById = (id: number) => {
	return service.get('/data-integrate/database/tables/' + id)
}

// Mine
export const getTablesById_v2 = (id: number) => {
	return service.get('/metadata/datasource/tables/' + id)
}

// 不要
export const getTableDataBySql = (id: number, sqlConsole: any) => {
	return service.post('/data-integrate/database/table-data/' + id, sqlConsole)
}

// 保留，牵扯众多
export const listDatabase = () => {
	return service.get('/data-integrate/datasource/list-all')
}

// 保留？？？
export const listTreeByIdApi = (id: any) => {
	return service.get('/data-integrate/database/list-tree/' + id)
}

// 保留？？？
export const listColumnsByIdAndTableName = (id: any, tableName: any) => {
	return service.get('/data-integrate/database/' + id + '/' + tableName + '/columns')
}

// 保留？？？
export const listColumnsByIdAndSql = (id: any, sqlConsole: any) => {
	return service.post('/data-integrate/database/' + id + '/sql/columns', sqlConsole)
}

// 保留？？？
export const getSqlGenerationApi = (id: any, tableName: any, tableRemarks) => {
	return service.get('/data-integrate/database/' + id + '/' + tableName + '/sql-generation?tableRemarks=' + tableRemarks)
}

// 保留？？？
export const listMiddleDbTreeApi = () => {
	return service.get('/data-integrate/database/middle-db/list-tree')
}

// 保留？？？
export const listMiddleDbColumnsApi = (tableName: any) => {
	return service.get('/data-integrate/database/middle-db/' + tableName + '/columns')
}

// 保留？？？
export const getMiddleDbSqlGenerationApi = (tableName: any, tableRemarks) => {
	return service.get('/data-integrate/database/middle-db/' + tableName + '/sql-generation?tableRemarks=' + tableRemarks)
}

// 保留？？？
export const getTableInfoApi = (tableName: any) => {
	return service.get('/data-integrate/database/middle-db/table-info/' + tableName)
}

// 保留？？？
export const saveTableInfoApi = (dataForm: any) => {
	return service.post('/data-integrate/database/middle-db/table-info', dataForm)
}

// 保留？？？
export const deleteTableInfoApi = (tableName: any) => {
	return service.delete('/data-integrate/database/middle-db/table-info/' + tableName)
}
